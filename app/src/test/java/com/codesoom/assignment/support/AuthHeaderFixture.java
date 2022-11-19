package com.codesoom.assignment.support;

public enum AuthHeaderFixture {
    INVALID_TOKEN_TYPE("Giibeom eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw"),
    INVALID_TOKEN_VALUE("Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"),
    VALID_TOKEN_VALUE("Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw");

    AuthHeaderFixture(String authorization) {
        this.authorization = authorization;
    }

    private final String authorization;

    public String 인증_헤더값() {
        return authorization;
    }
}
