package com.codesoom.assignment.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
class JwtUtilTest {

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String SECRET = "12345678901234567890123456789010";
    private final JwtUtil jwtUtil = new JwtUtil(SECRET);

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {
        @Nested
        @DisplayName("userId가 주어지면")
        class Context_with_userId {
            @Test
            @DisplayName("token 을 리턴한다")
            void it_returns_token() {
                String encode = jwtUtil.encode(1L);

                assertThat(encode).isEqualTo(INVALID_TOKEN);
            }
        }
    }
}
