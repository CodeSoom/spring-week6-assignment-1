package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilsTest {
    private static final String SECRET_KEY = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InFqYXdsc3FqYWNrc0BuYXZlci5jb20ifQ.Kp42APjRQt9BsUDief7z63Oz257gC7fbh47zyWsPrjo";
    public static final String GIVEN_EMAIL = "qjawlsqjacks@naver.com";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InFqYXdsc3FqYWNrc0BuYXZlci5jb20ifQ.Kp42APjRQt9BsUDief7z63Oz257gC7fbh47zyWsPrj";

    private JwtUtils utils;

    @BeforeEach
    void setUp() {
        utils = new JwtUtils(SECRET_KEY);
    }

    @Test
    void encodeTest() {
        String expectToken = utils.encode(GIVEN_EMAIL);

        assertThat(expectToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void decodeTest() {
        Claims decode = utils.decode(VALID_TOKEN);

        assertThat(GIVEN_EMAIL).isEqualTo(decode.get("email", String.class));
    }

    @Test
    void invalidSignature() {
        assertThatThrownBy(() -> utils.decode(INVALID_TOKEN))
                .isInstanceOf(SignatureException.class);
    }
}
