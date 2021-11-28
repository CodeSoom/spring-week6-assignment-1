package com.codesoom.assignment.dto;

import lombok.Builder;

@Builder
public class SessionResponseData {
    private final String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
