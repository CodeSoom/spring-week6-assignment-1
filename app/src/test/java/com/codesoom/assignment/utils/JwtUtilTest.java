package com.codesoom.assignment.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "INVALID";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @DisplayName("Jwt 유틸을 이용해 암호화 하였을 때, 유호한 토큰을 반환한다.")
    @Test
    void encode() {
        String token = jwtUtil.encode(1L);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @DisplayName("유효한 토큰으로 복호화 하였을 때, 유효한 유저 아이디를 반환한다.")
    @Test
    void decodeWithValidToken() {
       Claims claims = jwtUtil.decode(VALID_TOKEN);

       assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @DisplayName("유효하지 않은 토큰을 복호화 하였을 때, 예외를 던진다.")
    @Test
    void decodeWithInValidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
            .isInstanceOf(SignatureException.class);
    }
}
