package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.invalid";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {

        @Test
        @DisplayName("JWT를 발급해서 리턴한다")
        void It_returns_json_web_token() {
            String token = jwtUtil.encode(1L);

            assertThat(token).isEqualTo(VALID_TOKEN);
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {

        @Nested
        @DisplayName("유효한 시그니처를 가진 토큰이 주어진다면")
        class Context_with_valid_token {
            private final String validToken = VALID_TOKEN;

            @Test
            @DisplayName("디코딩된 페이로드를 리턴한다")
            void It_returns_jwt_body() {
                var cliams = jwtUtil.decode(validToken);

                assertThat(cliams.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 시그니처를 가진 토큰이 주어진다면")
        class Context_with_invalid_token {
            private final String invalidToken = INVALID_TOKEN;

            @Test
            @DisplayName("시그니처 예외를 던진다")
            void decode() {
                assertThatThrownBy(() -> jwtUtil.decode(invalidToken))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
