package com.codesoom.assignment.dto;

public class UserLoginData {
    private String email;
    private String password;

    public UserLoginData() {}

    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
