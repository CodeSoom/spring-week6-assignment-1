package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("JwtUtilTest 클래스")
class JwtUtilTest {
    final Long USER_ID = 1L;
    final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5VoLz2Ug0E6jQK_anP6RND1YHpzlBdxsR2ORgYef_aQ";
    final String MODULATED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5VoLz2Ug0E6jQK_anP6RND1YHpzlBdxsR2ORgYef_aQss";
    final String secretKey = "qwertyuiopqwertyuiopqwertyuiopqw";

    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secretKey);
    }

    @Nested
    @DisplayName("encode()")
    class Describe_encode {
        @Nested
        @DisplayName("user id가 주어졌을 때")
        class Context_with_user_id {
            Long givenUserId = USER_ID;

            @DisplayName("인코딩된 token을 반환한다.")
            @Test
            void it_returns_encoded_token() {
                String code = jwtUtil.encode(givenUserId);
                assertThat(code).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode()")
    class Describe_decode {
        @Nested
        @DisplayName("유효한 token이 주어졌을 때")
        class Context_with_token {
            String givenToken = VALID_TOKEN;

            @DisplayName("user id를 반환한다.")
            @Test
            void it_returns_user_id() {
                Long userId = jwtUtil.decode(givenToken);
                assertThat(userId).isEqualTo(USER_ID);
            }
        }

        @Nested
        @DisplayName("비어 있는 token이 주어졌을 때")
        class Context_blank_token {
            String givenToken = "";

            @DisplayName("유효하지 않은 토큰이 주어졌다는 예외를 던진다")
            @Test
            void it_throws_invalid_token_exception() {
                assertThrows(InvalidAccessTokenException.class, () -> jwtUtil.decode(givenToken));
            }
        }

        @Nested
        @DisplayName("null인 token이 주어졌을 때")
        class Context_null_token {
            String givenToken = null;

            @DisplayName("유효하지 않은 토큰이 주어졌다는 예외를 던진다")
            @Test
            void it_throws_invalid_token_exception() {
                assertThrows(InvalidAccessTokenException.class, () -> jwtUtil.decode(givenToken));
            }
        }

        @Nested
        @DisplayName("변조된 token이 주어졌을 때")
        class Context_modulated_token {
            String givenToken = MODULATED_TOKEN;

            @DisplayName("유효하지 않은 토큰이 주어졌다는 예외를 던진다")
            @Test
            void it_throws_invalid_token_exception() {
                assertThrows(InvalidAccessTokenException.class, () -> jwtUtil.decode(givenToken));
            }
        }
    }
}
