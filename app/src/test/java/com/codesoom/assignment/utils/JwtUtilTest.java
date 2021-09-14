package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.NotSupportedIdException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtil 테스트")
class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789010";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드")
    class Describe_encode {

        @Nested
        @DisplayName("유효한 id이 주어지면")
        class Context_with_valid_id {

            private final Long VALID_ID = 1L;
            private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

            @Test
            @DisplayName("암호화된 토큰을 반환한다.")
            void it_return_token() {
                String token = jwtUtil.encode(VALID_ID);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("Null 값이 주어지면")
        class Context_with_null {

            private Long NULL_VALUE = null;

            @Test
            @DisplayName("NotSupportedIdException을 던집니다.")
            void it_throw_NotSupportedIdException() {
                assertThatThrownBy(()->jwtUtil.encode(NULL_VALUE))
                        .isInstanceOf(NotSupportedIdException.class);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드")
    class Describe_decode {

        @Nested
        @DisplayName("유효한 token이 주어지면")
        class Context_with_valid_token {

            private final Long VALID_ID = 1L;
            private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

            @Test
            @DisplayName("복호화된 Claims을 반환한다.")
            void it_return_claims() {
                Claims token = jwtUtil.decode(VALID_TOKEN);
                assertThat(token.get("userId", Long.class)).isEqualTo(VALID_ID);
            }
        }

        @Nested
        @DisplayName("Null 값이 주어지면")
        class Context_with_null {

            private final String NULL_VALUE = null;

            @Test
            @DisplayName("InvalidAccessTokenException을 던집니다.")
            void it_throw_InvalidAccessTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode(NULL_VALUE))
                        .isInstanceOf(InvalidAccessTokenException.class);
            }
        }

        @Nested
        @DisplayName("빈 값이 주어지면")
        class Context_with_blank {

            private final String BLANK_VALUE = " ";

            @Test
            @DisplayName("InvalidAccessTokenException을 던집니다.")
            void it_throw_InvalidAccessTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode(BLANK_VALUE))
                        .isInstanceOf(InvalidAccessTokenException.class);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 token이 주어지면")
        class Context_with_invalid_token {

            private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
                    "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

            @Test
            @DisplayName("InvalidAccessTokenException을 던집니다.")
            void it_throw_InvalidAccessTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidAccessTokenException.class);
            }
        }
    }
}
