package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789012";
    private static final String USER_EMAIL = "userEmail";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJleGFtcGxlQGxvZ2l" +
                                              "uLmNvbSJ9.VC6A_h3VehY_zvmxNGtgdwCc6zeFRBcdKSkj2rNB93Y";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJleGFtcGxlQGxvZ2l" +
                                                "uLmNvbSJ9.VC6A_h3VehY_zvmxNGtgdwCc6zeFRBcdKSkj2rNB9aa";
    private static final String EXISTED_EMAIL = "example@login.com";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }


    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        @Nested
        @DisplayName("userEmail이 주어지면")
        class Context_with_userEmail {

            @Test
            @DisplayName("토큰을 리턴한다")
            void it_returns_token() {
                String token = jwtUtil.encode(EXISTED_EMAIL);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {

        @Nested
        @DisplayName("유효한 token이 주어지면")
        class Context_with_valid_token {

            @Test
            @DisplayName("클레임을 리턴한다")
            void it_returns_claims() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);
                assertThat(claims.get(USER_EMAIL)).isEqualTo(EXISTED_EMAIL);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 token이 주어지면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 리턴한다")
            void it_returns_invalidTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN)).isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("Null인 token이 주어지면")
        class Context_with_null_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 리턴한다")
            void it_returns_invalidTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode(null)).isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("비어있는 token이 주어지면")
        class Context_with_empty_or_blank_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 리턴한다")
            void it_returns_invalidTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode("")).isInstanceOf(InvalidTokenException.class);
                assertThatThrownBy(() -> jwtUtil.decode("  ")).isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
