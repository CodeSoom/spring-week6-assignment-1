package com.codesoom.assignment.dto;

public enum AuthorizationHeader {
    Bearer("Bearer ", "Bearer ".length()),
    ;
    private String authorizationValue;
    private int authorizationLength;

    AuthorizationHeader(String authorizationValue, int authorizationLength) {
        this.authorizationValue = authorizationValue;
        this.authorizationLength = authorizationLength;
    }

    public String getAuthorizationValue() {
        return authorizationValue;
    }

    public int getAuthorizationLength() {
        return authorizationLength;
    }
}
