package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private final String secret = "12345678901234567890123456789012";

    private final String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private final String inValidToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDA";

    private final Long givenUserId = 1L;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret);
    }

    @DisplayName("생성된 토큰을 리턴한다.")
    @Test
    void encode() {
        String token = jwtUtil.encode(givenUserId);

        assertThat(token).isEqualTo(validToken);
    }

    @DisplayName("유효한 토큰이 주어진다면 클레임을 리턴한다.")
    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(validToken);

        assertThat(claims.get("userId", Long.class)).isEqualTo(givenUserId);
    }

    @DisplayName("유효하지 않은 토큰이 주어진다면 '토큰이 유효하지 않다' 는 예외를 던진다.'")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    @CsvSource(inValidToken)
    void decodeWithInValidToken(String token) {
        assertThrows(InvalidTokenException.class, () -> jwtUtil.decode(token));
    }

}
