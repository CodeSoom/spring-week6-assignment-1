package com.codesoom.assignment.utils;


import com.codesoom.assignment.errors.InvalidAccessTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "WRONG";


    JwtUtil jwtUtil;


    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);

    }

    @Test
    void encode() {
        Long userId = 1L;
        String token = jwtUtil.encode(userId);

        Claims claims = jwtUtil.decode(token);

        assertThat(claims.get("userId", Long.class)).isEqualTo(userId);
    }

    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void decodeWithEmptyToken() {
        assertThatThrownBy(() -> jwtUtil.decode(null))
                .isInstanceOf(InvalidAccessTokenException.class);
        assertThatThrownBy(() -> jwtUtil.decode(""))
                .isInstanceOf(InvalidAccessTokenException.class);
        assertThatThrownBy(() -> jwtUtil.decode("   "))
                .isInstanceOf(InvalidAccessTokenException.class);
    }
}
