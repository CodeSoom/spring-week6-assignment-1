package com.codesoom.assignment.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtUtils")
class JwtUtilsTest {

    JwtUtils jwtUtils = new JwtUtils("12345678901234567890123456789010");

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
                String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
                Long userId = jwtUtils.decodeThenGetUserId(token);
                assertThat(userId).isEqualTo(1L);
            }
        }
    }
}
