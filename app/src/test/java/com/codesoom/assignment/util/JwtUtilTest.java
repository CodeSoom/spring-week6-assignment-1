package com.codesoom.assignment.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = VALID_TOKEN  + "WRONG";

    private static final String SECRET_KEY = "12345678901234567890123456789010";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET_KEY);
    }

    @Test
    void createTokenWithId() {
        String token = jwtUtil.createToken(1L);

        assertThat(token.contains(".")).isTrue();
    }

    @Test
    void checkTokenIsTrue() {
        boolean valid = jwtUtil.validateToken(VALID_TOKEN);

        assertThat(valid).isTrue();
    }

    @Test
    void checkTokenIsFalse() {
        boolean valid = jwtUtil.validateToken(INVALID_TOKEN);

        assertThat(valid).isFalse();
    }
}
