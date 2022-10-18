package com.codesoom.assignment.dto.response;

/**
 * 로그인 응답.
 */
public class LoginResponse {
    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
