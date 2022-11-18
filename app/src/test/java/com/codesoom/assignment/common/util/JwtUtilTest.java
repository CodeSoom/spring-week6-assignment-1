package com.codesoom.assignment.common.util;

import com.codesoom.assignment.domain.session.exception.InvalidTokenException;
import com.codesoom.assignment.domain.session.exception.TokenNotExistException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.codesoom.assignment.support.AuthHeaderFixture.INVALID_TOKEN_TYPE;
import static com.codesoom.assignment.support.AuthHeaderFixture.INVALID_TOKEN_VALUE;
import static com.codesoom.assignment.support.TokenFixture.ACCESS_TOKEN_1_INVALID;
import static com.codesoom.assignment.support.TokenFixture.ACCESS_TOKEN_1_INVALID_BLANK;
import static com.codesoom.assignment.support.TokenFixture.ACCESS_TOKEN_1_VALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WebMvcTest(JwtUtil.class)
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class encode_메서드는 {
        @Test
        @DisplayName("토큰을 secretKey로 인코딩하여 리턴한다")
        void it_returns_token() {
            String token = jwtUtil.encode(ACCESS_TOKEN_1_VALID.아이디());

            assertThat(token).isEqualTo(ACCESS_TOKEN_1_VALID.토큰());
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class decode_메서드는 {
        @Nested
        @DisplayName("유효한 토큰이 주어질 경우")
        class Context_with_valid_token {
            @Test
            @DisplayName("userId가 포함된 클레임을 리턴한다")
            void it_returns_claims() {
                Claims claims = jwtUtil.decode(ACCESS_TOKEN_1_VALID.토큰());

                assertThat(claims.get("userId", Long.class)).isEqualTo(ACCESS_TOKEN_1_VALID.아이디());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어질 경우")
        class Context_with_invalid_token {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(ACCESS_TOKEN_1_INVALID.토큰()))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰이 공백으로 주어질 경우")
        class Context_with_blank_token {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(ACCESS_TOKEN_1_INVALID_BLANK.토큰()))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class validateAccessToken_메서드는 {

        @Nested
        @DisplayName("null이 주어질 경우")
        class Context_with_null {
            @Test
            @DisplayName("TokenNotExistException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> jwtUtil.validateAccessToken(null))
                        .isInstanceOf(TokenNotExistException.class);
            }
        }

        @Nested
        @DisplayName("토큰 타입이 Bearer가 아닐 경우")
        class Context_with_no_start_bearer {
            @Test
            @DisplayName("InvalidTokenException 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> jwtUtil.validateAccessToken(INVALID_TOKEN_TYPE.인증_헤더값()))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰 타입이 Bearer일 경우")
        class Context_with_start_bearer {
            @Nested
            @DisplayName("유효하지 않은 토큰이 주어지면")
            class Context_with_valid_token {
                @Test
                @DisplayName("InvalidTokenException 예외를 던진다")
                void it_returns_exception() {
                    assertThatThrownBy(() -> jwtUtil.validateAccessToken(INVALID_TOKEN_VALUE.인증_헤더값()))
                            .isInstanceOf(InvalidTokenException.class);
                }
            }
        }
    }
}
