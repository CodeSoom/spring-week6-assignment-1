package com.codesoom.assignment.infra;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtils")
class JwtUtilsTest {

    JwtUtils jwtUtils = new JwtUtils("12345678901234567890123456789010");
    final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGV";
    final String BLANK_TOKEN = " ";

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        @Nested
        @DisplayName("정상적으으로 요청이 들어올 경우")
        class context_with_valid_request {

            @Test
            @DisplayName("엑세스토큰을 리턴한다. ")
            void it_returns_valid_accessToken() {
                String accessToken = jwtUtils.encode(1L);
                assertThat(accessToken).contains(".");
            }
        }
    }

    @Nested
    @DisplayName("decodeThenGetUserId 메소드는")
    class Describe_decodeThenGetUserId {

        @Nested
        @DisplayName("정상적인 토큰이 들어올 경우")
        class context_with_valid_token {

            @Test
            @DisplayName("userId를 반환한다. ")
            void it_returns_userId() {
                Claims claims = jwtUtils.decode(TOKEN);
                assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("비정상적인 토큰이 들어올 경우")
        class context_with_invalid_token {

            @ParameterizedTest
            @ValueSource(strings = {BLANK_TOKEN, INVALID_TOKEN})
            @NullAndEmptySource
            @DisplayName("InvalidTokenException 예외를 던진다.")
            void it_returns_InvalidTokenException(String text) {
                assertThatThrownBy(()-> jwtUtils.decode(text)).isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
