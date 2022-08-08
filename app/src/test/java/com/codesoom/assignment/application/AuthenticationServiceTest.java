package com.codesoom.assignment.application;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthenticationService 인터페이스의")
public class AuthenticationServiceTest {
    private String SECRET_KEY = "12345678901234567890123456789012";
    private final AuthenticationService service = new JwtService();

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {
        @Nested
        @DisplayName("유저 식별자가 주어지면")
        class Context_with_request {
            Long givenUserId = 1L;

            @Test
            @DisplayName("인증된 토큰을 리턴한다")
            void It_returns_token() {
                String expectToken = service.create(givenUserId);

                assertThat(givenUserId.toString()).isEqualTo(Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(expectToken)
                        .getBody()
                        .get("userId")
                        .toString());
            }
        }
    }
}
