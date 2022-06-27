package com.codesoom.assignment.auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtAuth")
class JwtAuthTest {
    private final String SECRET_STRING = "12345678901234567890123456789010";
    private final Long USER_ID = 1L;
    private final JwtKey key = new JwtKey(SECRET_STRING);
    private final ClaimTokenAuth<Claims> auth = new JwtAuth(key);

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        @Test
        @DisplayName(".을 포함한 문자열을 반환한다")
        void it_returns_string_containing_punctuation() {
            String token = auth.encode(USER_ID);

            assertThat(token).contains(".");
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {

        @Nested
        @DisplayName("토큰을 전달 받으면")
        class Context_with_token {
            private String token;

            @BeforeEach
            void setUp() {
                token = auth.encode(USER_ID);
            }

            @Test
            @DisplayName("userId가 포함된 Claims 타입을 반환한다.")
            void it_returns_claims_containing_userId() {
                Claims claims = auth.decode(token);

                assertThat(claims.get("userId")).isEqualTo(USER_ID.intValue());
            }
        }
    }
}
