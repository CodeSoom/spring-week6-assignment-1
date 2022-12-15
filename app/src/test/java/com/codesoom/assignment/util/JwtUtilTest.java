package com.codesoom.assignment.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JwtUtilTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnaab";

    private final JwtUtil jwtUtil = mock(JwtUtil.class);

    @BeforeEach
    void setUp() {
        given(jwtUtil.createToken(anyLong())).willReturn(VALID_TOKEN);

        given(jwtUtil.validateToken(VALID_TOKEN)).willReturn(Boolean.TRUE);

        given(jwtUtil.validateToken(INVALID_TOKEN)).willReturn(Boolean.FALSE);
    }

    @Test
    void createTokenWithId() {
        String token = jwtUtil.createToken(1L);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    void checkTokenIsTrue() {
        boolean valid = jwtUtil.validateToken(VALID_TOKEN);

        assertThat(valid).isTrue();
    }

    @Test
    void checkTokenIsFalse() {
        boolean valid = jwtUtil.validateToken(INVALID_TOKEN);

        assertThat(valid).isFalse();
    }
}
