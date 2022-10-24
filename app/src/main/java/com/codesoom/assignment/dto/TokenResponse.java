package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
