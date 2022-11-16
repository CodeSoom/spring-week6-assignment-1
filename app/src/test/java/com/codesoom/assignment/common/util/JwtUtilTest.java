package com.codesoom.assignment.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.codesoom.assignment.support.TokenFixture.ACCESS_TOKEN_1_VALID;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(JwtUtil.class)
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void it_returns_token() {
        String encode = jwtUtil.encode(ACCESS_TOKEN_1_VALID.아이디());

        assertThat(encode).isEqualTo(ACCESS_TOKEN_1_VALID.토큰());
    }
}
