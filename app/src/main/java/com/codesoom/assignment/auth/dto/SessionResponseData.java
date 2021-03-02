package com.codesoom.assignment.auth.dto;

import lombok.Getter;

@Getter
public class SessionResponseData {
    private String accessToken;

    public SessionResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
