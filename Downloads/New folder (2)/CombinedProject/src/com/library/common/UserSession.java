package com.library.common;

public class UserSession {
    private static UserSession instance;
    private int userId;
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

    public void login(int userId, String email, Role role) {
        this.userId = userId;
        this.userEmail = email;
        this.userRole = role;
    }

    public void logout() {
        this.userId = 0;
        this.userEmail = null;
        this.userRole = null;
    }

    public int getUserId() {
        return userId;
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
