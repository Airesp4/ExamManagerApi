package com.app.ExamManager.model;

public enum eUserRole {
    ADMIN("admin"),
    USER("user");

    private String role;

    eUserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
