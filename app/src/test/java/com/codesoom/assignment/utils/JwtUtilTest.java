package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String ACCESS_INVALID_TOKEN = ACCESS_TOKEN + "invalid";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encode() {

        String token = jwtUtil.encode(1L);

        assertThat(token).isEqualTo(ACCESS_TOKEN);

    }

    @Test
    void decodeWithValidToken() {

        Claims claims = jwtUtil.decode(ACCESS_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
        // TODO - > userId , verification

    }

    @Test
    void decodeWithInvalidToken() {

        Assertions.assertThatThrownBy(() -> jwtUtil.decode(ACCESS_INVALID_TOKEN)).isInstanceOf(SignatureException.class);

    }


}