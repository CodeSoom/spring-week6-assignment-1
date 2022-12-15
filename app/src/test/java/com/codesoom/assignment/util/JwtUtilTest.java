package com.codesoom.assignment.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtUtilTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnaab";


    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secretKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secretKey);
    }

    @Test
    void createTokenWithId() {
        String token = jwtUtil.createToken(1L);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    void getClaimsWithValidToken() {
        Claims claims = jwtUtil.getClaims(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class))
                .isEqualTo(1L);
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
