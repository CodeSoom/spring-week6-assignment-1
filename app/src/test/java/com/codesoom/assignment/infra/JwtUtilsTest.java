package com.codesoom.assignment.infra;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
                Assertions.assertThat(accessToken).contains(".");
            }
        }
    }
}
