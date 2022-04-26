package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class JsonWebTokenResponse {

    private final String accessToken;

    public JsonWebTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
