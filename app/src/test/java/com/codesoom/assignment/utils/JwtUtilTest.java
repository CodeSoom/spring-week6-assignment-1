package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private static final String secret = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

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
    void decodeWithBlankToken() {
        assertThatThrownBy(() -> jwtUtil.decode(null))
                .isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode(""))
                .isInstanceOf(InvalidAccessTokenException.class);
    }



}