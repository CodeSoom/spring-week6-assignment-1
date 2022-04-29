package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN.substring(0, VALID_TOKEN.length() - 1) + "Z";
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    @DisplayName("토큰 인코딩 테스트")
    void encode() {
        String accessToken = jwtUtil.encode(1L);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("유효한 토큰으로 디코딩 테스트")
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 디코딩 테스트")
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("빈 토큰으로 디코딩 테스트")
    void decodeWithEmptyToken() {
        assertThatThrownBy(() -> jwtUtil.decode(null))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode("  "))
                .isInstanceOf(InvalidTokenException.class);
    }
}