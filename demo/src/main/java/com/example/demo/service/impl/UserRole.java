package com.example.demo.service.impl;

public class UserRole {

    private int userId;
    private String login;
    private Role role;

    // Enum для ролей пользователя
    public enum Role {
        ADMIN,
        USER
    }

    // Конструктор для UserRole
    public UserRole(int userId, String login, Role role) {
        this.userId = userId;
        this.login = login;
        this.role = role;
    }

    // Геттеры
    public int getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public Role getRole() {
        return role;
    }
}
