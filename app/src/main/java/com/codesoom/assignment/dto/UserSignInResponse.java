package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class UserSignInResponse {

    private final String accessToken;

    public UserSignInResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
