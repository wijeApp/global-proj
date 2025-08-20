package com.tas.global.globalven.service;

import com.tas.global.globalven.UserRole;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthorizationService {
    
    // Define role hierarchy levels
    private static final int SUPER_ADMIN_LEVEL = 100;
    private static final int ADMIN_LEVEL = 80;
    private static final int HR_LEVEL = 60;
    private static final int MANAGER_LEVEL = 50;
    private static final int FINANCE_LEVEL = 40;
    private static final int USER_LEVEL = 10;
    
    /**
     * Check if user has access to User Management system
     * @param userRole The user's role
     * @return true if authorized, false otherwise
     */
    public boolean hasUserManagementAccess(String userRole) {
        if (userRole == null || userRole.trim().isEmpty()) {
            return false;
        }
        
        try {
            UserRole role = UserRole.valueOf(userRole.toUpperCase());
            return hasUserManagementAccess(role);
        } catch (IllegalArgumentException e) {
            return false; // Invalid role
        }
    }
    
    /**
     * Check if user has access to User Management system
     * @param userRole The user's role enum
     * @return true if authorized, false otherwise
     */
    public boolean hasUserManagementAccess(UserRole userRole) {
        if (userRole == null) {
            return false;
        }
        
        // Only these roles can access user management
        List<UserRole> authorizedRoles = Arrays.asList(
            UserRole.SUPER_ADMIN,
            UserRole.ADMIN,
            UserRole.HR,
            UserRole.MANAGER
        );
        
        return authorizedRoles.contains(userRole);
    }
    
    /**
     * Check if user can create other users
     * @param userRole The user's role
     * @return true if authorized, false otherwise
     */
    public boolean canCreateUsers(UserRole userRole) {
        if (userRole == null) {
            return false;
        }
        
        return userRole == UserRole.SUPER_ADMIN || 
               userRole == UserRole.ADMIN || 
               userRole == UserRole.HR;
    }
    
    /**
     * Check if user can delete other users
     * @param userRole The user's role
     * @return true if authorized, false otherwise
     */
    public boolean canDeleteUsers(UserRole userRole) {
        if (userRole == null) {
            return false;
        }
        
        return userRole == UserRole.SUPER_ADMIN || 
               userRole == UserRole.ADMIN;
    }
    
    /**
     * Check if user can modify roles of other users
     * @param userRole The user's role
     * @param targetRole The role being assigned
     * @return true if authorized, false otherwise
     */
    public boolean canModifyRole(UserRole userRole, UserRole targetRole) {
        if (userRole == null || targetRole == null) {
            return false;
        }
        
        int userLevel = getRoleLevel(userRole);
        int targetLevel = getRoleLevel(targetRole);
        
        // Can only assign roles of lower or equal level
        return userLevel >= targetLevel && userLevel >= MANAGER_LEVEL;
    }
    
    /**
     * Check if user can access financial transactions
     * @param userRole The user's role
     * @return true if authorized, false otherwise
     */
    public boolean hasFinancialAccess(UserRole userRole) {
        if (userRole == null) {
            return false;
        }
        
        return userRole == UserRole.SUPER_ADMIN || 
               userRole == UserRole.ADMIN || 
               userRole == UserRole.FINANCE ||
               userRole == UserRole.MANAGER;
    }
    
    /**
     * Check if user can approve transactions
     * @param userRole The user's role
     * @return true if authorized, false otherwise
     */
    public boolean canApproveTransactions(UserRole userRole) {
        if (userRole == null) {
            return false;
        }
        
        return userRole == UserRole.SUPER_ADMIN || 
               userRole == UserRole.ADMIN || 
               userRole == UserRole.MANAGER;
    }
    
    /**
     * Check if user can access HR functions
     * @param userRole The user's role
     * @return true if authorized, false otherwise
     */
    public boolean hasHRAccess(UserRole userRole) {
        if (userRole == null) {
            return false;
        }
        
        return userRole == UserRole.SUPER_ADMIN || 
               userRole == UserRole.ADMIN || 
               userRole == UserRole.HR ||
               userRole == UserRole.MANAGER;
    }
    
    /**
     * Get the access level for a role
     * @param role The user role
     * @return The access level (higher number = more privileges)
     */
    private int getRoleLevel(UserRole role) {
        switch (role) {
            case SUPER_ADMIN:
                return SUPER_ADMIN_LEVEL;
            case ADMIN:
                return ADMIN_LEVEL;
            case HR:
                return HR_LEVEL;
            case MANAGER:
                return MANAGER_LEVEL;
            case FINANCE:
                return FINANCE_LEVEL;
            case USER:
                return USER_LEVEL;
            default:
                return 0;
        }
    }
    
    /**
     * Get user-friendly error message for unauthorized access
     * @param resource The resource being accessed
     * @param userRole The user's current role
     * @return Error message
     */
    public String getUnauthorizedMessage(String resource, UserRole userRole) {
        String currentRole = userRole != null ? userRole.toString() : "GUEST";
        
        switch (resource.toLowerCase()) {
            case "user-management":
                return String.format("Access to User Management requires Administrator, HR, or Manager privileges. Current role: %s", currentRole);
            case "financial":
                return String.format("Access to Financial data requires Finance, Manager, or Administrator privileges. Current role: %s", currentRole);
            case "hr":
                return String.format("Access to HR functions requires HR, Manager, or Administrator privileges. Current role: %s", currentRole);
            default:
                return String.format("Insufficient privileges to access %s. Current role: %s", resource, currentRole);
        }
    }
    
    /**
     * Get list of required roles for a resource
     * @param resource The resource being accessed
     * @return List of authorized roles
     */
    public List<UserRole> getRequiredRoles(String resource) {
        switch (resource.toLowerCase()) {
            case "user-management":
                return Arrays.asList(UserRole.SUPER_ADMIN, UserRole.ADMIN, UserRole.HR, UserRole.MANAGER);
            case "financial":
                return Arrays.asList(UserRole.SUPER_ADMIN, UserRole.ADMIN, UserRole.FINANCE, UserRole.MANAGER);
            case "hr":
                return Arrays.asList(UserRole.SUPER_ADMIN, UserRole.ADMIN, UserRole.HR, UserRole.MANAGER);
            default:
                return Arrays.asList(UserRole.SUPER_ADMIN, UserRole.ADMIN);
        }
    }
}

