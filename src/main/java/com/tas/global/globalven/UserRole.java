package com.tas.global.globalven;

public enum UserRole {
    USER("User"),
    ADMIN("Administrator"),
    SUPER_ADMIN("Super Administrator"),
    MANAGER("Manager"),
    HR("Human Resources"),
    FINANCE("Finance Officer");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getValue() {
        return this.name();
    }
}



