package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserNotFoundException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "WRONG";

    private static final Long USER_ID = 1L;
    private static final Long NOT_EXIST_ID = 100L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        @Nested
        @DisplayName("사용자 ID가 주어진다면")
        class Context_with_user_id {

            @Test
            @DisplayName("생성된 토큰을 반환한다")
            void encode() {
                String token = jwtUtil.encode(USER_ID);

                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 사용자 ID가 주어진다면")
        class Context_with_invalid_user_id {

            @Test
            @DisplayName("예외를 던진다")
            void encode() {
                assertThatThrownBy(() -> jwtUtil.encode(NOT_EXIST_ID))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {

        @Nested
        @DisplayName("유효한 토큰이 주어진다면")
        class Context_with_valid_token {

            @Test
            @DisplayName("Claim을 반환한다")
            void It_returns_claim() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어진다면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("예외를 던진다")
            void It_throws_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰이 주어지지 않는다면")
        class Context_without_token {

            @Test
            @DisplayName("예외를 던진다")
            void It_throws_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(""))
                        .isInstanceOf(InvalidTokenException.class);

                assertThatThrownBy(() -> jwtUtil.decode("  "))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
