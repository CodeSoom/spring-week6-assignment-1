package com.codesoom.assignment.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;

public class JwtUtilTest {
    private static final String SECRET = "01234567890123456789012345678912";
    public static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.Vid3sIDrCUySUo7pLyfNpqGaAO3kI3FgGMWyRX11sRE";
    public static final String INVALID_TOKEN = VALID_TOKEN + "INVALID";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        this.jwtUtil = new JwtUtil(SECRET);


    }

    @Test
    void encode() {
        final String token = jwtUtil.encode(1L);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
            .isInstanceOf(SignatureException.class);
    }



    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);
        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    // @Test
    // void decodeWithBlankToken() {
    //     Claims claims = jwtUtil.decode("");
    //     assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    // }
}
