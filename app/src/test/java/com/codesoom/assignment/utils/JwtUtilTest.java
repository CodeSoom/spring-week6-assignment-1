package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @DisplayName("userId로 인코딩해서 얻은 토큰이 유효한 토큰과 일치하는지 확인한다")
    void encode() {
        String accessToken = jwtUtil.encode(1L);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("유효한 토큰을 디코딩하면 claims의 'userId'가 주어진 userId와 같은지 확인한다")
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    @DisplayName("유효하지 않은 토큰을 디코딩하면 예외를 던진다")
    void decodeWithInvalidToken() {
        List<String> tokens = Arrays.asList(INVALID_TOKEN, "a", "a.b", "a.b.c", "eyJhbGciOiJIUzI1NiJ9", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9");

        for(int i = 0; i < tokens.size(); i++){
            String token = tokens.get(i);
            assertThatThrownBy(() -> jwtUtil.decode(token))
                    .isInstanceOf(InvalidTokenException.class);
        }
    }

    @Test
    @DisplayName("null 또는 빈 토큰을 디코딩하면 예외를 던진다")
    void decodeWithEmptyToken() {
        List<String> tokens = Arrays.asList(null, "", "  ");

        for(int i = 0; i < tokens.size(); i++){
            String token = tokens.get(i);
            assertThatThrownBy(() -> jwtUtil.decode(token))
                    .isInstanceOf(InvalidTokenException.class);
        }
    }
}