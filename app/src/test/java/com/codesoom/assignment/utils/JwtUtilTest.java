package com.codesoom.assignment.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.errors.InvalidTokenException;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.repository.query.Param;

public class JwtUtilTest {
    public static final String SECRET = "01234567890123456789012345678912";
    public static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.Vid3sIDrCUySUo7pLyfNpqGaAO3kI3FgGMWyRX11sRE";
    public static final String INVALID_TOKEN = VALID_TOKEN + "INVALID";
    public static final Long USER_ID = 1L;

    private final JwtUtil jwtUtil;

    public JwtUtilTest() {
        this.jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encode() {
        final String token = jwtUtil.encode(USER_ID);
        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @ParameterizedTest
    @CsvSource(
        value = {"null", "''", "'   '"}, nullValues = {"null"}
    )
    void decodeWithEmptyToken(final String token) {
        assertThatThrownBy(() -> jwtUtil.decode(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);
        assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
    }
}
