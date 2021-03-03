package com.codesoom.assignment.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final String SECRET = "12345678901234567890123456789012";

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private JwtUtil jwtUtil;

    private final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    @DisplayName("Jwt encode()는 userId가 주어지면 생성된 토큰을 리턴한다")
    void encode() {

        String token = jwtUtil.encode(USER_ID);
        assertThat(token).isEqualTo(VALID_TOKEN);
    }
}
