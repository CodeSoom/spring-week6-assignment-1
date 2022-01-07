package com.codesoom.assignment.utils;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtUtil 테스트")
public class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJ1c2VySWQiOjF9.0c0dUJvb0GnVGxFzaYhbAKxTdxGDydO5wgnBeQjLK7U";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJ1c2VySWQiOjF9.0c0dUJvb0GnVGxFzaYhbAKxTdxGDydO5wgnBeQj1234";
    private static final Long USER_ID = 1L;

    private JwtUtil jwtUtil;

    @BeforeEach
    void prepareJwtUtil() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @DisplayName("encode 메서드는")
    @Nested
    class Describe_encode {
        @DisplayName("유저 id가 주어진다면")
        @Nested
        class Context_with_user_id {

            @DisplayName("JWT 토큰을 리턴한다")
            @Test
            void it_returns_jwt_token() {
                assertThat(jwtUtil.encode(USER_ID)).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @DisplayName("decode 메서드는")
    @Nested
    class Describe_decode {
        @DisplayName("JWT 토큰이 주어진다면")
        @Nested
        class Context_with_jwt_token {
            @DisplayName("userId가 포함허는 Claims를 리턴한다")
            @Test
            void it_returns_claims() {
                assertThat(jwtUtil.decode(VALID_TOKEN).get("userId", Long.class)).isEqualTo(USER_ID);
            }
        }
    }
}
