package com.codesoom.assignment.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JWT 클래스")
class JWTTest {
    private final String key = "11111111111111111111111111111111";
    private final JWT jwt = new JWT(key);

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {
        private final Map<String, Object> givenClaims = new HashMap<>() {{
            put("key", "value");
        }};
        private final Map<String, Object> givenHeaders = new HashMap<>() {{
            put("alg", "HS256");
        }};

        @Test
        @DisplayName("jwt 토큰을 리턴한다.")
        void It_returns_jwt_token() {
            assertThat(jwt.encode(givenHeaders, givenClaims)).isEqualTo(
                    "eyJhbGciOiJIUzI1NiJ9."
                            + "eyJrZXkiOiJ2YWx1ZSJ9."
                            + "oVwICBWJOb_ggP34mBdxQJumkUGaBYdargu_3um3JsQ"
            );
        }
    }
}
