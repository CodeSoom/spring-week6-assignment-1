package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BearerAuthorization {
    private static final String BEARER = "Bearer ";
    private final String token;

    public String getToken() {
        return token;
    }

    public BearerAuthorization(String authorization) {
        token = authorization.substring(BEARER.length());
    }
}
