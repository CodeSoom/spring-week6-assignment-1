package com.codesoom.assignment.auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtAuth")
class JwtAuthTest {
    private final String SECRET_STRING = "12345678901234567890123456789010";
    private final JwtKey key = new JwtKey(SECRET_STRING);
    private final ClaimTokenAuth<Claims> auth = new JwtAuth(key);

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        @Test
        @DisplayName(".을 포함한 문자열을 반환한다")
        void it_returns_string_which_contains_punctuation() {
            String token = auth.encode(1L);

            assertThat(token).contains(".");
        }
    }
}
