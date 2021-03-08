package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JWT 클래스")
class JWTTest {
    private final String key = "11111111111111111111111111111111";
    private final JWT jwt = new JWT(key);

    private final Map<String, Object> givenBody = new HashMap<>() {{
        put("key", "value");
    }};
    private final Map<String, Object> givenHeaders = new HashMap<>() {{
        put("alg", "HS256");
    }};

    private final String token = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJrZXkiOiJ2YWx1ZSJ9."
            + "oVwICBWJOb_ggP34mBdxQJumkUGaBYdargu_3um3JsQ";

    private final String invalidToken = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJrZXkiOiJ2YWx1ZSJ9."
            + "oVwICBWJOb_ggP34mBdxQJumkUGaBYdargu_3um3JsE";

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {

        @Test
        @DisplayName("jwt 토큰을 리턴한다.")
        void It_returns_jwt_token() {
            assertThat(jwt.encode(givenHeaders, givenBody)).isEqualTo(token);
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {
        @Nested
        @DisplayName("주어진 토큰이 올바를 때")
        class Context_given_token_is_valid {
            @Test
            @DisplayName("jwt 토큰의 body 를 리턴한다.")
            void It_returns_jwt_body() {
                Jws<Claims> claims = jwt.decode(token);
                JwsHeader headers = claims.getHeader();
                Claims body = claims.getBody();

                assertThat(headers.get("alg")).isEqualTo(givenHeaders.get("alg"));
                assertThat(body.get("key")).isEqualTo(givenBody.get("key"));
            }
        }

        @Nested
        @DisplayName("주어진 토큰이 올바르지 않을 때")
        class Context_given_token_is_invalid {
            @Test
            @DisplayName("올바른 토큰이 아니라는 예외를 던진다.")
            void It_throws_invalid_token_exception() {
                assertThatThrownBy(() -> jwt.decode(invalidToken))
                        .isInstanceOf(SignatureException.class);
            }
        }
    }
}
