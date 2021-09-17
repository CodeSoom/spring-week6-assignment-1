package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private static final String secret = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = VALID_TOKEN + "wrong";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret);
    }

    @Test
    void encode() {
        String accessToken = jwtUtil.encode(1L);
        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);
        Long userId = claims.get("userId", Long.class);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void decodeWithBlankOrNullToken(String token) {
            assertThatThrownBy(() -> jwtUtil.decode(token))
                    .isInstanceOf(InvalidAccessTokenException.class);
    }
}
