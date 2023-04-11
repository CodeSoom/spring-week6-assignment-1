package com.codesoom.assignment.infra;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtils")
class JwtUtilsTest {

    JwtUtils jwtUtils = new JwtUtils();

    @Nested
    @DisplayName("createToken 메소드는")
    class Describe_Create {

        @Nested
        @DisplayName("정상적으으로 요청이 들어올 경우")
        class context_with_valid_request {

            @Test
            @DisplayName("엑세스토큰을 리턴한다. ")
            void it_returns_valid_accessToken() {

                String accessToken = jwtUtils.createToken();
                Assertions.assertThat(accessToken).contains(".");

            }
        }
    }

}
