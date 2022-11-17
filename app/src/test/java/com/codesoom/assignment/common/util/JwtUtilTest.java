package com.codesoom.assignment.common.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.codesoom.assignment.support.TokenFixture.ACCESS_TOKEN_1_VALID;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(JwtUtil.class)
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class encode_메서드는 {
        @Test
        @DisplayName("토큰을 secretKey로 인코딩하여 리턴한다")
        void it_returns_token() {
            String token = jwtUtil.encode(ACCESS_TOKEN_1_VALID.아이디());

            assertThat(token).isEqualTo(ACCESS_TOKEN_1_VALID.토큰());
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class decode_메서드는 {
        @Nested
        @DisplayName("유효한 토큰이 주어질 경우")
        class Context_with_blank_token {
            @Test
            @DisplayName("userId가 포함된 클레임을 리턴한다")
            void it_returns_exception() {
                Claims claims = jwtUtil.decode(ACCESS_TOKEN_1_VALID.토큰());

                assertThat(claims.get("userId", Long.class)).isEqualTo(ACCESS_TOKEN_1_VALID.아이디());
            }
        }
    }
}
