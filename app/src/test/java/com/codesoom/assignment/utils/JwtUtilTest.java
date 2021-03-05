package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789010";

    private static final String VALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJ1c2VySWQiOjF9."
            + "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final String INVALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJ1c2VySWQiOjF9."
            + "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGG3";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @DisplayName("encode에 회원 id가 주어지면 생성된 토큰을 리턴한다.")
    @Test
    void encode() {
        String token = jwtUtil.encode(1L);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @DisplayName("decode에 유효한 토큰이 주어지면 클레임을 리턴한다.")
    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @DisplayName("decode에 유요하지 않은 토큰이 주어지면 예외를 던진다.")
    @ParameterizedTest
    @NullSource
    @CsvSource(INVALID_TOKEN)
    void decodeWithInValidToken(String token) {
        assertThrows(InvalidAccessTokenException.class, () -> jwtUtil.decode(token));
    }
}
