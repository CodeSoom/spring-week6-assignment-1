package com.codesoom.assignment.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtUtilTest 클래스")
class JwtUtilTest {
    final Long USER_ID = 1L;
    final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5VoLz2Ug0E6jQK_anP6RND1YHpzlBdxsR2ORgYef_aQ";
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
        @DisplayName("token이 주어졌을 때")
        class Context_with_token {
            String givenToken = VALID_TOKEN;

            @DisplayName("user id를 반환한다.")
            @Test
            void it_returns_user_id() {
                Long userId = jwtUtil.decode(givenToken);
                assertThat(userId).isEqualTo(USER_ID);
            }
        }
    }
}
