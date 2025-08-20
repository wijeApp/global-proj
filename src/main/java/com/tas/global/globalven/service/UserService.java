package com.tas.global.globalven.service;

import com.tas.global.globalven.User;
import com.tas.global.globalven.UserRole;
import com.tas.global.globalven.UserStatus;
import com.tas.global.globalven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Create or Update User
    public User saveUser(User user) {
        if (user.getId() == null) {
            // New user - encode password
            user.setCreatedDate(LocalDateTime.now());
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                // Only encode if not already encoded
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            // Existing user - check if password needs encoding
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isPresent() && user.getPassword() != null && 
                !user.getPassword().equals(existingUser.get().getPassword()) &&
                !user.getPassword().startsWith("$2a$")) {
                // Password changed and not already encoded
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        user.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Get user by username or email (for login)
    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail);
    }
    
    // Check if username exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    // Check if email exists
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    // Get users by role
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }
    
    // Get users by status
    public List<User> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }
    
    // Get users by role and status
    public List<User> getUsersByRoleAndStatus(UserRole role, UserStatus status) {
        return userRepository.findByRoleAndStatus(role, status);
    }
    
    // Get users by department
    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }
    
    // Search users by name
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }
    
    // Search users by full name
    public List<User> searchUsersByFullName(String fullName) {
        return userRepository.findByFullNameContaining(fullName);
    }
    
    // Get active users
    public List<User> getActiveUsers() {
        return userRepository.findActiveUsers();
    }
    
    // Get admin users
    public List<User> getAdminUsers() {
        return userRepository.findAdminUsers();
    }
    
    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Update user status
    public User updateUserStatus(Long id, UserStatus status) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(status);
            user.setUpdatedDate(LocalDateTime.now());
            return userRepository.save(user);
        }
        return null;
    }
    
    // Update user role
    public User updateUserRole(Long id, UserRole role) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(role);
            user.setUpdatedDate(LocalDateTime.now());
            return userRepository.save(user);
        }
        return null;
    }
    
    // Update last login
    public void updateLastLogin(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    // Get user statistics
    public Map<String, Long> getUserStatistics() {
        return Map.of(
            "total", userRepository.count(),
            "active", userRepository.countByStatus(UserStatus.ACTIVE),
            "inactive", userRepository.countByStatus(UserStatus.INACTIVE),
            "admins", userRepository.countByRole(UserRole.ADMIN) + userRepository.countByRole(UserRole.SUPER_ADMIN),
            "users", userRepository.countByRole(UserRole.USER)
        );
    }
    
    // Get users with no login
    public List<User> getUsersWithNoLogin() {
        return userRepository.findUsersWithNoLogin();
    }
    
    // Get recent users (created in last 30 days)
    public List<User> getRecentUsers() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return userRepository.findByCreatedDateBetween(thirtyDaysAgo, LocalDateTime.now());
    }
    
    // Validate user for creation/update
    public void validateUser(User user) throws IllegalArgumentException {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        
        // Check for duplicate username (only for new users or when username is changed)
        if (user.getId() == null || !getUserById(user.getId()).map(User::getUsername).equals(user.getUsername())) {
            if (existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
        }
        
        // Check for duplicate email (only for new users or when email is changed)
        if (user.getId() == null || !getUserById(user.getId()).map(User::getEmail).equals(user.getEmail())) {
            if (existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
    }
}



