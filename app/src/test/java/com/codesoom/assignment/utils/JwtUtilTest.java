package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@AutoConfigureMockMvc
class JwtUtilTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
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

                assertThat(encode).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("userId가 주어지지 않으면")
        class Context_without_userId {
            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(
                        () -> jwtUtil.encode(null)
                ).isExactlyInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {
        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {
            @Test
            @DisplayName("userId를 리턴한다")
            void it_returns_userId() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);
                Long userId = claims.get("userId", Long.class);

                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_invalid_token {
            private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGwz";

            @ParameterizedTest
            @DisplayName("유효하지 않은 토큰이라는 예외를 던진다")
            @NullSource
            @EmptySource
            @ValueSource(strings = {INVALID_TOKEN, "   "})
            void it_throws_exception(String invalidToken) {
                assertThatThrownBy(
                        () -> jwtUtil.decode(invalidToken)
                ).isExactlyInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
