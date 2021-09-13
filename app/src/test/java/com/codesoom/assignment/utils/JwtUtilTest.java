package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789010";

    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJIeW91bmdVayJ9.RBmbXHWmnXrbl0DeVOvHl6fiPmkLQi1Z5MruzMl9RkQ";

    private static final String ACCESS_INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJIeW91bmdVayJ9.RBmbXHWmnXrbl0DeVOvHl6fiPmkLQi1Z5MruzMl9RkQ" + "invalid";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encode() {

        String token = jwtUtil.encode(1L);

        assertThat(token).isEqualTo(ACCESS_TOKEN);

    }

    @Test
    void decodeWithValidToken() {

        Claims claims = jwtUtil.decode(ACCESS_TOKEN);

        assertThat(claims.get("userId", String.class)).isEqualTo("HyoungUk");
        // TODO - > userId , verification

    }

    @Test
    void decodeWithInvalidToken() {

        Assertions.assertThatThrownBy(() -> jwtUtil.decode(ACCESS_INVALID_TOKEN)).isInstanceOf(SignatureException.class);

    }


}