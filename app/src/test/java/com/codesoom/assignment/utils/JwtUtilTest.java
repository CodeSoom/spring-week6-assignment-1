package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private JwtUtil jwUtil;

    @BeforeEach
    void setUp() {
        jwUtil = new JwtUtil(SECRET);
    }

    @Test
    void encode() {
        String token = jwUtil.encode(1L);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    void decodeWithValidToken() {
        Claims claims = jwUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);

        // TODO -> userId, verification
    }

    @Test
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void decodeWithEmptyToken() {
        assertThatThrownBy(() -> jwUtil.decode(null))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwUtil.decode(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwUtil.decode("   "))
                .isInstanceOf(InvalidTokenException.class);
    }
}
