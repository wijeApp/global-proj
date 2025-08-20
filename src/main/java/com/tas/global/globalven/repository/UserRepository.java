package com.tas.global.globalven.repository;

import com.tas.global.globalven.User;
import com.tas.global.globalven.UserRole;
import com.tas.global.globalven.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find by username
    Optional<User> findByUsername(String username);
    
    // Find by email
    Optional<User> findByEmail(String email);
    
    // Find by username or email for login
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find by role
    List<User> findByRole(UserRole role);
    
    // Find by status
    List<User> findByStatus(UserStatus status);
    
    // Find by role and status
    List<User> findByRoleAndStatus(UserRole role, UserStatus status);
    
    // Find by department
    List<User> findByDepartment(String department);
    
    // Find by employee ID
    Optional<User> findByEmployeeId(Long employeeId);
    
    // Search by name (first name or last name)
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContaining(@Param("name") String name);
    
    // Search by full name
    @Query("SELECT u FROM User u WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<User> findByFullNameContaining(@Param("fullName") String fullName);
    
    // Find active users
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();
    
    // Find admin users
    @Query("SELECT u FROM User u WHERE u.role IN ('ADMIN', 'SUPER_ADMIN')")
    List<User> findAdminUsers();
    
    // Find users created between dates
    List<User> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find users by created by
    List<User> findByCreatedBy(String createdBy);
    
    // Count by role
    long countByRole(UserRole role);
    
    // Count by status
    long countByStatus(UserStatus status);
    
    // Find users with last login after date
    List<User> findByLastLoginAfter(LocalDateTime date);
    
    // Find users with no login (null last login)
    @Query("SELECT u FROM User u WHERE u.lastLogin IS NULL")
    List<User> findUsersWithNoLogin();
}



