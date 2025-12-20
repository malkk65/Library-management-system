package com.library.common;

public class UserSession {
    private static UserSession instance;
    private String userEmail;
    private Role userRole;

    public enum Role {
        ADMIN,
        STAFF,
        MEMBER
    }

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(String email, Role role) {
        this.userEmail = email;
        this.userRole = role;
    }

    public void logout() {
        this.userEmail = null;
        this.userRole = null;
    }

    public boolean isLoggedIn() {
        return userEmail != null;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Role getUserRole() {
        return userRole;
    }
}
