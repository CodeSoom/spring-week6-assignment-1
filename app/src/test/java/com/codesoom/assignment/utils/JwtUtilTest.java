package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    @BeforeEach
    public void setUp(){
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    public void encode(){
        String accessToken = jwtUtil.customEncode(1L);

        assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    public void decode(){
        Claims claims = jwtUtil.customDecode(ACCESS_TOKEN);

        assertThat(claims.get("userId",Long.class)).isEqualTo(1L);
    }
}