package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private final String SECRET = "12345678901234567890123456789012";
    private final long VALID_TIME = 60 * 60 * 1000;
    private final Long USER_ID = 1L;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, VALID_TIME);
    }

    @DisplayName("encode 메서드에 userId가 주어지면 생성된 토큰을 리턴한다.")
    @Test
    void encode() {
        String token = jwtUtil.encode(USER_ID);

        assertThat(token).contains(".");
    }

}
