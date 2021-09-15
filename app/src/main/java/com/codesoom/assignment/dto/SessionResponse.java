package com.codesoom.assignment.dto;

import lombok.Getter;

public class SessionResponse {
    @Getter
    private String accessToken;

    public SessionResponse() {}

    public SessionResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
