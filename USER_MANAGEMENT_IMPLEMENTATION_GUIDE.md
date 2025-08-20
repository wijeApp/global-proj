# User Management Implementation Guide

## Overview
This guide provides step-by-step instructions for implementing the user management and authorization system in the GlobalVen application. It covers both backend and frontend implementation details.

## Table of Contents
1. [Backend Implementation](#backend-implementation)
2. [Frontend Implementation](#frontend-implementation)
3. [Database Setup](#database-setup)
4. [Security Configuration](#security-configuration)
5. [Testing Strategy](#testing-strategy)
6. [Deployment Checklist](#deployment-checklist)

## Backend Implementation

### 1. Dependencies Required

Add these dependencies to your `pom.xml`:

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT Support (Optional) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Password Validation -->
<dependency>
    <groupId>org.passay</groupId>
    <artifactId>passay</artifactId>
    <version>1.6.2</version>
</dependency>
```

### 2. Entity Modifications

Ensure your User entity has proper annotations:

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    @JsonIgnore // Never send password to frontend
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;
    
    // ... other fields
}
```

### 3. Service Layer Implementation

#### UserService Enhancements

```java
@Service
@Transactional
public class UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        // Validate user data
        validateNewUser(user);
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set defaults
        user.setCreatedDate(LocalDateTime.now());
        user.setStatus(UserStatus.PENDING);
        
        // Save and return
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, User updates) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Check permissions
        checkUpdatePermissions(getCurrentUser(), user, updates);
        
        // Update allowed fields
        if (updates.getFirstName() != null) {
            user.setFirstName(updates.getFirstName());
        }
        if (updates.getLastName() != null) {
            user.setLastName(updates.getLastName());
        }
        // ... other fields
        
        user.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    private void validateNewUser(User user) {
        // Username validation
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ValidationException("Username already exists");
        }
        
        // Email validation
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        
        // Password strength validation
        validatePasswordStrength(user.getPassword());
    }
    
    private void validatePasswordStrength(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
            new LengthRule(8, 100),
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1),
            new CharacterRule(EnglishCharacterData.Special, 1),
            new WhitespaceRule()
        ));
        
        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            throw new ValidationException("Password does not meet requirements");
        }
    }
}
```

### 4. Controller Implementation with Authorization

```java
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // Only ADMIN and SUPER_ADMIN can view all users
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<User> users = userService.getAllUsers(pageable);
        
        // Remove passwords from response
        users.forEach(user -> user.setPassword(null));
        
        return ResponseEntity.ok(users);
    }
    
    // Create user - ADMIN and SUPER_ADMIN only
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User created = userService.createUser(user);
        created.setPassword(null); // Don't return password
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // Update user role - SUPER_ADMIN only
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        
        String newRole = request.get("role");
        User updated = userService.updateUserRole(id, UserRole.valueOf(newRole));
        updated.setPassword(null);
        return ResponseEntity.ok(updated);
    }
    
    // Delete user - SUPER_ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 5. Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/", "/login", "/register").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                
                // Role-based endpoints
                .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .requestMatchers("/api/employees/**").hasAnyRole("HR", "ADMIN", "SUPER_ADMIN", "MANAGER")
                .requestMatchers("/api/transfers/**").hasAnyRole("FINANCE", "ADMIN", "SUPER_ADMIN")
                
                // All other requests need authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/api/auth/login")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error")
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID")
            );
            
        return http.build();
    }
}
```

### 6. Custom UserDetailsService

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) 
            throws UsernameNotFoundException {
        
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> 
                new UsernameNotFoundException("User not found: " + usernameOrEmail));
        
        // Check if account is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new DisabledException("User account is not active");
        }
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        return createUserPrincipal(user);
    }
    
    private UserPrincipal createUserPrincipal(User user) {
        List<GrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
        
        return new UserPrincipal(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            authorities,
            user.getStatus() == UserStatus.ACTIVE
        );
    }
}
```

## Frontend Implementation

### 1. Authentication Service (JavaScript)

```javascript
// auth.service.js
class AuthService {
    constructor() {
        this.baseUrl = '/api/auth';
        this.tokenKey = 'auth_token';
        this.userKey = 'current_user';
    }
    
    async login(credentials) {
        try {
            const response = await fetch(`${this.baseUrl}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(credentials)
            });
            
            if (response.ok) {
                const data = await response.json();
                this.setSession(data);
                return { success: true, data };
            } else {
                const error = await response.json();
                return { success: false, error: error.message };
            }
        } catch (error) {
            return { success: false, error: 'Network error' };
        }
    }
    
    logout() {
        localStorage.removeItem(this.tokenKey);
        localStorage.removeItem(this.userKey);
        window.location.href = '/login';
    }
    
    setSession(authData) {
        localStorage.setItem(this.tokenKey, authData.token);
        localStorage.setItem(this.userKey, JSON.stringify({
            id: authData.id,
            username: authData.username,
            email: authData.email,
            roles: authData.roles
        }));
    }
    
    getToken() {
        return localStorage.getItem(this.tokenKey);
    }
    
    getCurrentUser() {
        const userStr = localStorage.getItem(this.userKey);
        return userStr ? JSON.parse(userStr) : null;
    }
    
    hasRole(role) {
        const user = this.getCurrentUser();
        return user && user.roles.includes(`ROLE_${role}`);
    }
    
    hasAnyRole(...roles) {
        const user = this.getCurrentUser();
        if (!user) return false;
        return roles.some(role => user.roles.includes(`ROLE_${role}`));
    }
    
    isAuthenticated() {
        return !!this.getToken();
    }
}

// Create singleton instance
const authService = new AuthService();
```

### 2. HTTP Interceptor

```javascript
// http.interceptor.js
class HttpInterceptor {
    constructor() {
        this.setupInterceptors();
    }
    
    setupInterceptors() {
        // Add auth token to all requests
        const originalFetch = window.fetch;
        window.fetch = async (url, options = {}) => {
            // Add auth header if token exists
            const token = authService.getToken();
            if (token) {
                options.headers = {
                    ...options.headers,
                    'Authorization': `Bearer ${token}`
                };
            }
            
            // Make request
            const response = await originalFetch(url, options);
            
            // Handle 401 unauthorized
            if (response.status === 401) {
                authService.logout();
                return response;
            }
            
            return response;
        };
        
        // Setup axios interceptors if using axios
        if (window.axios) {
            // Request interceptor
            axios.interceptors.request.use(
                config => {
                    const token = authService.getToken();
                    if (token) {
                        config.headers.Authorization = `Bearer ${token}`;
                    }
                    return config;
                },
                error => Promise.reject(error)
            );
            
            // Response interceptor
            axios.interceptors.response.use(
                response => response,
                error => {
                    if (error.response && error.response.status === 401) {
                        authService.logout();
                    }
                    return Promise.reject(error);
                }
            );
        }
    }
}

// Initialize interceptor
new HttpInterceptor();
```

### 3. Role-Based UI Components

```javascript
// ui-auth.js
class UIAuth {
    static init() {
        // Hide/show elements based on roles
        document.addEventListener('DOMContentLoaded', () => {
            this.applyRoleBasedVisibility();
            this.applyRoleBasedDisabling();
            this.displayUserInfo();
        });
    }
    
    static applyRoleBasedVisibility() {
        // Elements that require specific roles
        const roleElements = document.querySelectorAll('[data-roles]');
        
        roleElements.forEach(element => {
            const requiredRoles = element.dataset.roles.split(',');
            const hasAccess = authService.hasAnyRole(...requiredRoles);
            
            if (!hasAccess) {
                element.style.display = 'none';
            }
        });
        
        // Elements that require authentication
        const authElements = document.querySelectorAll('[data-auth-required]');
        authElements.forEach(element => {
            if (!authService.isAuthenticated()) {
                element.style.display = 'none';
            }
        });
    }
    
    static applyRoleBasedDisabling() {
        // Disable form elements based on roles
        const restrictedInputs = document.querySelectorAll('[data-edit-roles]');
        
        restrictedInputs.forEach(input => {
            const allowedRoles = input.dataset.editRoles.split(',');
            const canEdit = authService.hasAnyRole(...allowedRoles);
            
            if (!canEdit) {
                input.disabled = true;
                input.classList.add('readonly');
            }
        });
    }
    
    static displayUserInfo() {
        const user = authService.getCurrentUser();
        if (user) {
            // Update username displays
            document.querySelectorAll('.current-username').forEach(element => {
                element.textContent = user.username;
            });
            
            // Update role displays
            document.querySelectorAll('.current-role').forEach(element => {
                element.textContent = user.roles[0].replace('ROLE_', '');
            });
        }
    }
}

// Initialize UI auth
UIAuth.init();
```

### 4. Protected Route Handler

```javascript
// route-guard.js
class RouteGuard {
    static protectRoute(requiredRoles = []) {
        if (!authService.isAuthenticated()) {
            window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
            return false;
        }
        
        if (requiredRoles.length > 0 && !authService.hasAnyRole(...requiredRoles)) {
            window.location.href = '/unauthorized';
            return false;
        }
        
        return true;
    }
    
    static init() {
        // Get current page's required roles from meta tag
        const metaRoles = document.querySelector('meta[name="required-roles"]');
        if (metaRoles) {
            const roles = metaRoles.content.split(',').filter(r => r);
            this.protectRoute(roles);
        }
    }
}

// Auto-protect current page
RouteGuard.init();
```

## Database Setup

### 1. Migration Scripts

```sql
-- V1__Create_users_table.sql
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    phone_number VARCHAR(20),
    profile_image VARCHAR(255),
    last_login DATETIME,
    failed_login_attempts INT DEFAULT 0,
    locked_until DATETIME,
    password_changed_at DATETIME,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    department VARCHAR(50),
    employee_id BIGINT,
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_status (status),
    INDEX idx_department (department),
    
    CONSTRAINT fk_employee 
        FOREIGN KEY (employee_id) 
        REFERENCES employees(id) 
        ON DELETE SET NULL
);

-- V2__Create_audit_log_table.sql
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    username VARCHAR(50),
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    success BOOLEAN DEFAULT TRUE,
    error_message TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_created_at (created_at),
    
    CONSTRAINT fk_audit_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE SET NULL
);

-- V3__Create_user_sessions_table.sql
CREATE TABLE IF NOT EXISTS user_sessions (
    id VARCHAR(128) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    last_activity DATETIME NOT NULL,
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_expires_at (expires_at),
    
    CONSTRAINT fk_session_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE
);
```

### 2. Initial Data Script

```sql
-- V4__Insert_default_users.sql
-- Password: Admin@123 (BCrypt encoded)
INSERT INTO users (username, email, password, first_name, last_name, role, status, department, created_by)
VALUES 
('admin', 'admin@globalven.com', '$2a$10$YourBcryptHashHere', 'System', 'Administrator', 'SUPER_ADMIN', 'ACTIVE', 'IT', 'SYSTEM'),
('hradmin', 'hr@globalven.com', '$2a$10$YourBcryptHashHere', 'HR', 'Admin', 'HR', 'ACTIVE', 'HR', 'SYSTEM'),
('financeadmin', 'finance@globalven.com', '$2a$10$YourBcryptHashHere', 'Finance', 'Admin', 'FINANCE', 'ACTIVE', 'Finance', 'SYSTEM');
```

## Security Configuration

### 1. Application Properties

```properties
# Security settings
security.jwt.secret=your-secret-key-change-in-production
security.jwt.expiration=86400000
security.password.min-length=8
security.password.max-age-days=90
security.login.max-attempts=5
security.login.lock-duration-minutes=30

# Session settings
server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=true

# CORS settings
cors.allowed-origins=http://localhost:3000,http://localhost:8080
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.exposed-headers=Authorization
cors.allow-credentials=true
```

### 2. Security Headers Configuration

```java
@Component
public class SecurityHeadersFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Security headers
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        httpResponse.setHeader("X-Frame-Options", "DENY");
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        httpResponse.setHeader("Content-Security-Policy", "default-src 'self'");
        
        chain.doFilter(request, response);
    }
}
```

## Testing Strategy

### 1. Unit Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers_WithAdminRole_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllUsers_WithUserRole_ShouldForbid() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void testGetAllUsers_WithoutAuth_ShouldUnauthorize() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized());
    }
}
```

### 2. Integration Tests

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testLoginFlow() {
        // Test login
        LoginRequest loginRequest = new LoginRequest("admin", "Admin@123");
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
            "/api/auth/login", loginRequest, JwtResponse.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getToken()).isNotNull();
        
        // Test authenticated request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + response.getBody().getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        ResponseEntity<User[]> usersResponse = restTemplate.exchange(
            "/api/users", HttpMethod.GET, entity, User[].class);
        
        assertThat(usersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
```

## Deployment Checklist

### Pre-Deployment
- [ ] Change all default passwords
- [ ] Update JWT secret key
- [ ] Configure proper CORS settings
- [ ] Enable HTTPS
- [ ] Set secure cookie flags
- [ ] Configure proper session timeout
- [ ] Review and update security headers
- [ ] Enable audit logging
- [ ] Set up database backups
- [ ] Configure rate limiting

### Database
- [ ] Run all migration scripts
- [ ] Create database indexes
- [ ] Set up proper database permissions
- [ ] Configure connection pooling
- [ ] Set up replication (if needed)

### Application
- [ ] Build with production profile
- [ ] Minimize and obfuscate JavaScript
- [ ] Enable GZIP compression
- [ ] Configure proper logging levels
- [ ] Set up monitoring and alerting
- [ ] Configure health checks
- [ ] Set up SSL certificates

### Security
- [ ] Run security scan
- [ ] Test all authorization rules
- [ ] Verify password policies
- [ ] Test session management
- [ ] Check for SQL injection vulnerabilities
- [ ] Test XSS protection
- [ ] Verify CSRF protection
- [ ] Test rate limiting

### Post-Deployment
- [ ] Verify all endpoints are working
- [ ] Test user login flow
- [ ] Verify role-based access
- [ ] Check audit logs
- [ ] Monitor error logs
- [ ] Test backup restoration
- [ ] Document deployment process

---

*Last Updated: [Current Date]*
*Version: 1.0*
