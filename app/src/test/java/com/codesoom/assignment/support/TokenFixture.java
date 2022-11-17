package com.codesoom.assignment.support;

public enum TokenFixture {
    ACCESS_TOKEN_1_VALID("giibeomIsA2YearsBackendDeveloper", 1L , "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.u-4fczk36juKx46aceMNuz2WJJJj1STWPjfLQA-Z5xY"),
    ACCESS_TOKEN_1_INVALID("giibeomIsA2YearsBackendDeveloper", 1L, "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"),
    ACCESS_TOKEN_1_INVALID_BLANK("giibeomIsA2YearsBackendDeveloper", 1L, "                        ");

    private final String secretKey;
    private final Long id;
    private final String token;

    TokenFixture(String secretKey, Long id, String token) {
        this.secretKey = secretKey;
        this.id = id;
        this.token = token;
    }

    public String 시크릿_키() {
        return secretKey;
    }

    public Long 아이디() {
        return id;
    }

    public String 토큰() {
        return token;
    }
}
