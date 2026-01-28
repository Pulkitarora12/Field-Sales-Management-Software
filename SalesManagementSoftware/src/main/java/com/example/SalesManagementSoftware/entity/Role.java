package com.example.SalesManagementSoftware.entity;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"), 
    EMPLOYEE("ROLE_EMPLOYEE");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
    
    public String getDisplayName() {
        return this.name().toLowerCase();
    }
}