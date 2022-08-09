package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String VALID_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final String INVALID_TOKEN = VALID_TOKEN + "WRONG";
    private JwtUtil jwtUtill;

    @BeforeEach
    void setUp() {
        jwtUtill = new JwtUtil(SECRET);
    }

    @Test
    void encode() {
        String token = jwtUtill.encode(1L);
        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtill.decode(VALID_TOKEN);
        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtill.decode(INVALID_TOKEN))
                .isInstanceOf(SignatureException.class);
    }
}
