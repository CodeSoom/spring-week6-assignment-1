package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilsTest {
    private static final String SECRET_KEY = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    public static final Long GIVEN_ID = 1L;
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGe";

    private JwtUtils utils;

    @BeforeEach
    void setUp() {
        utils = new JwtUtils(SECRET_KEY);
    }

    @Test
    void encodeTest() {
        String expectToken = utils.encode(GIVEN_ID);

        assertThat(expectToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void decodeTest() {
        Claims decode = utils.decode(VALID_TOKEN);

        assertThat(GIVEN_ID).isEqualTo(decode.get("userId", Long.class));
    }

    @Test
    void invalidSignature() {
        assertThatThrownBy(() -> utils.decode(INVALID_TOKEN))
                .isInstanceOf(SignatureException.class);
    }
}
