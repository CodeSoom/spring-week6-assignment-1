package com.codesoom.assignment.dto.response;

/**
 * 로그인 응답.
 */
public class LoginResponse {
    private final String accessToken;

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
