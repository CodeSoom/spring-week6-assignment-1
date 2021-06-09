package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil")
class JwtUtilTest {
    private final static String SECRETE_KEY = "12345678901234567890123456789010";
    private final static String VALID_TOKEN= "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".cjDHNEbvUC6G7AORn068kENYHYnOTIaMsgjD0Yyygn4";
    private final static String INVALID_TOKEN= "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".cjDHNEbvUC6G7AORn068kENYHYnOTIaMsgjD0Yyyg11";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRETE_KEY);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_Encode {
        @Test
        @DisplayName("사용자 아이디로 토큰을 생성한다.")
        void createValidToken() {
            String accessToken = jwtUtil.encode(1L);
            assertThat(accessToken).isEqualTo(VALID_TOKEN);
        }

        @Test
        @DisplayName("사용자 아이디가 Null이면 InvalidTokenException을 던진다. ")
        void createInvalidToken() {
            assertThatThrownBy(() -> {
                jwtUtil.encode(null);
            }).isInstanceOf(InvalidTokenException.class);
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_Decode {
        @Nested
        @DisplayName("정확한 토큰이 입력된다면")
        class Context_Valid_Token_Decode {
            @Test
            @DisplayName("사용자 아이디를 확인할 수 있다.")
            void valid_decode_token_find_userId() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);
                assertThat(claims.get("userId", Long.class))
                        .isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("잘못된 토큰을 입력 한다면")
        class Context_Invalid_Token_Decode {
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            void invalid_token_decode_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("빈 토큰 값을 입력 한다면")
        class Context_Empty_Token_Decode {
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            void empty_token_decode() {
                assertAll(
                        () -> {
                            assertThatThrownBy(() -> {
                                jwtUtil.decode("");
                            });
                        },
                        () -> {
                            assertThatThrownBy(() -> {
                                jwtUtil.decode(" ");
                            });
                        },
                        () -> {
                            assertThatThrownBy(() -> {
                                jwtUtil.decode(null);
                            });
                        }
                );
            }
        }
    }
}
