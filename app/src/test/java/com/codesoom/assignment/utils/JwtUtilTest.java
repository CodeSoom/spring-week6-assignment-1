package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnCCa";
    private static final String BLANK_TOKEN = "";
    private static final long USER_ID = 1L;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    @DisplayName("User Id를 JWT 암호화를 사용하여 Access Token을 반환한다")
    void encode() {
        // when
        String accessToken = jwtUtil.encode(USER_ID);

        // then
        assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    @DisplayName("Access Token을 JWT로 복호화 하여 User Id를 반환한다")
    void decodeWithValidToken() {
        // when
        Claims claims = jwtUtil.decode(ACCESS_TOKEN);

        // then
        assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("JWT 복호화 실패 - InValidToken")
    void decodeWithInValidToken() {
        // when
        // then
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);

    }

    @Test
    @DisplayName("JWT 복호화 실패 - BlankToken")
    void decodeWithBlankToken() {
        // when
        // then
        assertThatThrownBy(() -> jwtUtil.decode(BLANK_TOKEN))
                .isInstanceOf(InvalidTokenException.class);

    }
}
