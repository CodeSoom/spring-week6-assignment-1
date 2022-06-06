package com.codesoom.assignment.helper;

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
}