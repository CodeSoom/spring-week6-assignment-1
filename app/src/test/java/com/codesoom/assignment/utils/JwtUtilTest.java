package com.codesoom.assignment.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilTest {
    private static String SECRET = "12345678912345678912345678900012";
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encode() {
        String jwt = jwtUtil.encode(1L);
        assertThat(jwt).contains(".");
    }
}
