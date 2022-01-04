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

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private JwtUtil jwUtil;

    @BeforeEach
    void setUp() {
        jwUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {
        @Nested
        @DisplayName("userId가 주어질때 ")
        class Context_userId {

            private final Long USER_ID = 1L;

            @Test
            @DisplayName("토큰을 리턴한다.")
            void it_return_token() {
                String token = jwUtil.encode(USER_ID);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {
        @Nested
        @DisplayName("올바른 토큰값이 주어질 때")
        class Context_with_validToken {

            private final Long USER_ID = 1L;

            @Test
            @DisplayName("userId를 리턴한다.")
            void it_return_userId() {
                Claims claims = jwUtil.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);

            }
        }

        @Nested
        @DisplayName("올바르지 않는 토큰값이 주어진다면")
        class Context_with_invalidToken {

            @Test
            @DisplayName("InvalidTokenException을 리턴한다.")
            void it_return_InvalidTokenException() {
                assertThatThrownBy(() -> jwUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰값이 null이 주어진다면")
        class Context_with_null_token {

            @Test
            @DisplayName("InvalidTokenException을 리턴한다.")
            void it_return_InvalidTokenException() {
                assertThatThrownBy(() -> jwUtil.decode(null))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰값이 비어있다면")
        class Context_with_emptyToken {

            @Test
            @DisplayName("InvalidTokenException을 리턴한다.")
            void it_return_InvalidTokenException() {
                assertThatThrownBy(() -> jwUtil.decode(""))
                        .isInstanceOf(InvalidTokenException.class);

                assertThatThrownBy(() -> jwUtil.decode("   "))
                        .isInstanceOf(InvalidTokenException.class);

            }
        }
    }
}
