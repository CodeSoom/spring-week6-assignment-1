package com.codesoom.assignment.helper;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthJwtHelperTest {

    private final AuthJwtHelper authJwtHelper
            = new AuthJwtHelper("12345678901234567890123456789010");

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        @Test
        @DisplayName("x+.x+.x+ 패턴의 문자열 토큰을 반환한다")
        void it_returns_access_token() {
            String token = authJwtHelper.encode(1L);

            assertThat(token)
                    .matches(".+\\..+\\..+");
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_invalid_token {
            private String validToken;

            @BeforeEach
            void setUp() {
                validToken = authJwtHelper.encode(1L);
            }

            @Test
            @DisplayName("userId를 담은 claims을 반환한다.")
            void it_returns_claims_containing_userId() {
                Claims claims = authJwtHelper.decode(validToken);

                assertThat(claims.get("userId", Long.class))
                        .isEqualTo(1L);
            }
        }
    }
}
