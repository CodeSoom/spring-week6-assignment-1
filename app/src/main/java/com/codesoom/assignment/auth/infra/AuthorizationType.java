package com.codesoom.assignment.auth.infra;

/**
 * 인증 타입.
 */
public enum AuthorizationType {
    BASIC,
    BEARER;

    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
