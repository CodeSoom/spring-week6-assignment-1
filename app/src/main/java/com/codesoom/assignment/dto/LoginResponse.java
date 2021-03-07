package com.codesoom.assignment.dto;

public class LoginResponse {
    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getAccessToken() {
        return token;
    }
}
