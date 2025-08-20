# User Management and Authorization System Documentation

## Table of Contents
1. [Overview](#overview)
2. [User Roles and Permissions](#user-roles-and-permissions)
3. [Authorization Levels](#authorization-levels)
4. [Module Access Matrix](#module-access-matrix)
5. [API Endpoint Authorization](#api-endpoint-authorization)
6. [Database Schema](#database-schema)
7. [Implementation Guidelines](#implementation-guidelines)
8. [Security Best Practices](#security-best-practices)

## Overview

The GlobalVen application implements a comprehensive role-based access control (RBAC) system to manage user permissions and secure access to various modules. This document outlines the authorization structure that should be implemented throughout the application.

## User Roles and Permissions

### 1. SUPER_ADMIN (Super Administrator)
**Description**: Highest level of system access with complete control over all aspects of the application.

**Permissions**:
- Full CRUD operations on all modules
- User management (create, modify, delete all users including other admins)
- System configuration and settings
- Access to all reports and analytics
- Database maintenance operations
- Audit log access
- Can override any restriction

**Typical Users**: System administrators, IT administrators

### 2. ADMIN (Administrator)
**Description**: Administrative access with most system privileges except critical system operations.

**Permissions**:
- User management (cannot modify Super Admins)
- Access to all business modules
- Generate and view all reports
- Modify system settings (limited)
- View audit logs
- Cannot delete critical data

**Typical Users**: Department heads, senior managers

### 3. MANAGER
**Description**: Mid-level access focused on oversight and reporting capabilities.

**Permissions**:
- Read-only access to employee data
- Read-only access to financial data
- Can generate reports for their department
- Can view team performance metrics
- Cannot modify system data
- Can approve/reject requests within scope

**Typical Users**: Team leaders, supervisors

### 4. HR (Human Resources)
**Description**: Specialized access for human resources management.

**Permissions**:
- Full CRUD on employee records
- Access to employee reports
- Manage employee documents
- View and update employee status
- No access to financial modules
- Can generate HR-specific reports

**Typical Users**: HR staff, recruiters

### 5. FINANCE (Finance Officer)
**Description**: Specialized access for financial operations.

**Permissions**:
- Full CRUD on financial transactions
- Manage exchange rates
- Process transfers and payments
- Generate financial reports
- No access to employee personal data
- Can view employee financial data (salaries, benefits)

**Typical Users**: Accountants, finance staff

### 6. USER (Regular User)
**Description**: Basic access level for general users.

**Permissions**:
- View own profile
- Read-only access to public data
- Submit requests
- View assigned tasks
- Cannot modify any system data
- Limited report generation

**Typical Users**: Regular employees, contractors

## Authorization Levels

### Level 1: Public Access
- No authentication required
- Access to: Login page, Registration page, Public information

### Level 2: Authenticated Access
- Requires valid login
- Access to: Personal dashboard, Profile management, Basic features

### Level 3: Role-Based Access
- Requires specific role assignment
- Access determined by role permissions
- Module-specific restrictions apply

### Level 4: Data-Level Security
- Row-level security based on department/team
- Users can only access data within their scope
- Managers see only their team's data

## Module Access Matrix

| Module | SUPER_ADMIN | ADMIN | MANAGER | HR | FINANCE | USER |
|--------|-------------|-------|---------|-----|---------|------|
| **User Management** |
| Create Users | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ |
| Modify Users | ✓ | ✓* | ✗ | ✗ | ✗ | ✗ |
| Delete Users | ✓ | ✓* | ✗ | ✗ | ✗ | ✗ |
| View Users | ✓ | ✓ | ✓ | ✓ | ✗ | ✗ |
| **Employee Management** |
| Create Employees | ✓ | ✓ | ✗ | ✓ | ✗ | ✗ |
| Modify Employees | ✓ | ✓ | ✗ | ✓ | ✗ | ✗ |
| Delete Employees | ✓ | ✓ | ✗ | ✓ | ✗ | ✗ |
| View Employees | ✓ | ✓ | ✓ | ✓ | ✗ | ✗ |
| **Financial Operations** |
| Create Transactions | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ |
| Modify Transactions | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ |
| Delete Transactions | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ |
| View Transactions | ✓ | ✓ | ✓ | ✗ | ✓ | ✗ |
| **Rate Management** |
| Create Rates | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ |
| Modify Rates | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ |
| Delete Rates | ✓ | ✓ | ✗ | ✗ | ✓ | ✗ |
| View Rates | ✓ | ✓ | ✓ | ✗ | ✓ | ✓ |
| **Categories** |
| Manage Categories | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ |
| View Categories | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Reports** |
| Generate All Reports | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ |
| Department Reports | ✓ | ✓ | ✓ | ✗ | ✗ | ✗ |
| HR Reports | ✓ | ✓ | ✗ | ✓ | ✗ | ✗ |
| Financial Reports | ✓ | ✓ | ✓ | ✗ | ✓ | ✗ |
| **System Settings** |
| Modify Settings | ✓ | ✓* | ✗ | ✗ | ✗ | ✗ |
| View Audit Logs | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ |

*Limited access - cannot modify Super Admin accounts or critical settings

## API Endpoint Authorization

### Public Endpoints (No Authentication Required)
```
POST /api/auth/login          - User login
POST /api/auth/register       - User registration (if enabled)
GET  /api/public/*           - Public resources
```

### User Management Endpoints
```
GET    /api/users            - Required Roles: ADMIN, SUPER_ADMIN
POST   /api/users            - Required Roles: ADMIN, SUPER_ADMIN
GET    /api/users/{id}       - Required Roles: ADMIN, SUPER_ADMIN
PUT    /api/users/{id}       - Required Roles: ADMIN, SUPER_ADMIN
DELETE /api/users/{id}       - Required Roles: SUPER_ADMIN
PUT    /api/users/{id}/role  - Required Roles: SUPER_ADMIN
PUT    /api/users/{id}/status - Required Roles: ADMIN, SUPER_ADMIN
```

### Employee Management Endpoints
```
GET    /api/employees        - Required Roles: HR, ADMIN, SUPER_ADMIN, MANAGER
POST   /api/employees        - Required Roles: HR, ADMIN, SUPER_ADMIN
GET    /api/employees/{id}   - Required Roles: HR, ADMIN, SUPER_ADMIN, MANAGER
PUT    /api/employees/{id}   - Required Roles: HR, ADMIN, SUPER_ADMIN
DELETE /api/employees/{id}   - Required Roles: HR, ADMIN, SUPER_ADMIN
GET    /api/employees/search - Required Roles: HR, ADMIN, SUPER_ADMIN, MANAGER
```

### Financial Operations Endpoints
```
GET    /api/transfers        - Required Roles: FINANCE, ADMIN, SUPER_ADMIN, MANAGER
POST   /api/transfers        - Required Roles: FINANCE, ADMIN, SUPER_ADMIN
GET    /api/transfers/{id}   - Required Roles: FINANCE, ADMIN, SUPER_ADMIN, MANAGER
PUT    /api/transfers/{id}   - Required Roles: FINANCE, ADMIN, SUPER_ADMIN
DELETE /api/transfers/{id}   - Required Roles: FINANCE, ADMIN, SUPER_ADMIN

GET    /api/rates           - Required Roles: FINANCE, ADMIN, SUPER_ADMIN, MANAGER, USER
POST   /api/rates           - Required Roles: FINANCE, ADMIN, SUPER_ADMIN
PUT    /api/rates/{id}      - Required Roles: FINANCE, ADMIN, SUPER_ADMIN
DELETE /api/rates/{id}      - Required Roles: FINANCE, ADMIN, SUPER_ADMIN
```

### Dashboard and Reports Endpoints
```
GET    /api/dashboard/stats  - Required: Authenticated
GET    /api/reports/users    - Required Roles: ADMIN, SUPER_ADMIN
GET    /api/reports/employees - Required Roles: HR, ADMIN, SUPER_ADMIN
GET    /api/reports/financial - Required Roles: FINANCE, ADMIN, SUPER_ADMIN
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('USER', 'ADMIN', 'SUPER_ADMIN', 'MANAGER', 'HR', 'FINANCE') NOT NULL DEFAULT 'USER',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    phone_number VARCHAR(20),
    profile_image VARCHAR(255),
    last_login DATETIME,
    created_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    department VARCHAR(50),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE SET NULL
);
```

### Session/Token Management (If using database sessions)
```sql
CREATE TABLE user_sessions (
    id VARCHAR(255) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(500),
    expires_at DATETIME NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### Audit Log Table
```sql
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
```

## Implementation Guidelines

### 1. Authentication Flow
```
1. User submits credentials
2. System validates credentials
3. Generate session/JWT token
4. Return token with user details and roles
5. Client stores token for subsequent requests
6. Include token in all API requests
```

### 2. Authorization Check Process
```
1. Extract token from request
2. Validate token authenticity
3. Extract user role from token
4. Check role against endpoint requirements
5. Apply additional business rules if needed
6. Allow or deny access
```

### 3. Frontend Implementation
```javascript
// Check if user has required role
function hasRole(requiredRole) {
    const userRoles = getCurrentUserRoles();
    return userRoles.includes(requiredRole);
}

// Show/hide UI elements based on role
if (hasRole('ADMIN') || hasRole('SUPER_ADMIN')) {
    showAdminMenu();
}

// Protect routes
function protectRoute(requiredRoles) {
    const userRoles = getCurrentUserRoles();
    const hasAccess = requiredRoles.some(role => userRoles.includes(role));
    if (!hasAccess) {
        redirectToUnauthorized();
    }
}
```

### 4. Backend Implementation (Conceptual)
```java
// Example: Protect endpoint with role check
@GetMapping("/api/users")
@RequireRoles({"ADMIN", "SUPER_ADMIN"})
public ResponseEntity<List<User>> getAllUsers() {
    // Implementation
}

// Example: Data-level security
public List<Employee> getEmployees(User currentUser) {
    if (currentUser.hasRole("MANAGER")) {
        // Return only employees in manager's department
        return employeeRepository.findByDepartment(currentUser.getDepartment());
    } else if (currentUser.hasAnyRole("ADMIN", "SUPER_ADMIN", "HR")) {
        // Return all employees
        return employeeRepository.findAll();
    }
    // Return empty list for unauthorized users
    return Collections.emptyList();
}
```

## Security Best Practices

### 1. Password Security
- Enforce strong password requirements (min 8 chars, uppercase, lowercase, number, special char)
- Implement password hashing (BCrypt, Argon2, or PBKDF2)
- Never store plain text passwords
- Implement password expiration policy (90 days)
- Maintain password history (prevent reuse of last 5 passwords)

### 2. Session Management
- Implement session timeout (30 minutes of inactivity)
- Secure session tokens (HttpOnly, Secure flags)
- Implement single sign-on if multiple systems
- Log session activities
- Allow users to view active sessions

### 3. Authentication Security
- Implement account lockout after failed attempts (5 attempts = 30 min lock)
- Use CAPTCHA after 3 failed attempts
- Implement two-factor authentication for admin roles
- Log all authentication attempts
- Send email notifications for suspicious activities

### 4. Authorization Security
- Implement principle of least privilege
- Regular role and permission audits
- Log all authorization failures
- Implement emergency access procedures
- Document all role changes

### 5. API Security
- Use HTTPS for all communications
- Implement rate limiting
- Validate all inputs
- Use parameterized queries
- Implement CORS properly

### 6. Audit and Compliance
- Log all CRUD operations
- Log all authentication attempts
- Log all authorization failures
- Implement data retention policies
- Regular security audits

### 7. Data Protection
- Encrypt sensitive data at rest
- Implement field-level encryption for PII
- Use secure communication channels
- Implement data masking for non-production environments
- Regular backups with encryption

## Default System Accounts

For initial system setup, the following default accounts should be created:

| Username | Role | Default Password | Notes |
|----------|------|------------------|--------|
| superadmin | SUPER_ADMIN | ChangeMeNow! | Change immediately |
| admin | ADMIN | AdminPass123! | System administration |
| hradmin | HR | HRPass123! | HR operations |
| financeadmin | FINANCE | FinPass123! | Financial operations |
| manager1 | MANAGER | Manager123! | Test manager account |
| user1 | USER | User123! | Test user account |

**Important**: All default passwords must be changed on first login!

## Compliance Considerations

1. **GDPR Compliance** (if applicable)
   - Right to access personal data
   - Right to rectification
   - Right to erasure
   - Data portability

2. **SOX Compliance** (if applicable)
   - Segregation of duties
   - Access controls
   - Audit trails
   - Change management

3. **Industry Standards**
   - Follow OWASP guidelines
   - Implement ISO 27001 controls
   - Regular penetration testing
   - Security awareness training

---

*This document should be reviewed and updated regularly to reflect changes in security requirements and business needs.*
