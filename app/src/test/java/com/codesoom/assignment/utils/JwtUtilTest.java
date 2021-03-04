package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET ="12345678901234567890123456789010";
    private static final String TOKEN= "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encode() {
        String token = jwtUtil.encode(1L);
        assertThat(token).isEqualTo(TOKEN);
    }

    @Test
    void decode(){
        Claims claims = jwtUtil.decode(TOKEN);
        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

}