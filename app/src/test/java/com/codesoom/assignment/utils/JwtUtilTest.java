package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {
    final String secret = "12345678901234567890123456789012";
    final String accessTokenValue
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret);
    }

    @Test
    void encode() {
        String token = jwtUtil.encode(1L);

        assertThat(token).isEqualTo(accessTokenValue);
    }

    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(accessTokenValue);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }
}
