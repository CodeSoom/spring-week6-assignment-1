package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.UserNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private final String VALID_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private final String INVALID_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGG1";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        @Nested
        @DisplayName("아이디를 받으면")
        class Context_with_a_valid_id {

            private Long userId = 1L;

            @Test
            @DisplayName("토큰을 생성하여 반환한다.")
            void it_returns_a_token() {
                String accessToken = jwtUtil.encode(userId);
                assertThat(accessToken).isEqualTo(VALID_ACCESS_TOKEN);
            }
        }

        @Nested
        @DisplayName("null 아이디를 받으면")
        class Context_with_a_invalid_id {

            private Long userId = null;

            @Test
            @DisplayName("예외를 던진다.")
            void it_returns_a_token() {
                assertThatThrownBy(() -> jwtUtil.encode(userId))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {

        @Nested
        @DisplayName("토큰을 받으면")
        class Context_with_a_valid_token {

            @Test
            @DisplayName("검증하여 인증정보를 반환한다.")
            void it_returns_a_token() {
                Claims claims = jwtUtil.decode(VALID_ACCESS_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰을 받으면")
        class Context_with_a_invalid_id {

            @Test
            @DisplayName("예외를 던진다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_ACCESS_TOKEN))
                        .isInstanceOf(SignatureException.class);
            }
        }
    }
}