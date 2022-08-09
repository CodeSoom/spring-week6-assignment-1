package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthenticationService 인터페이스의")
public class AuthenticationServiceTest {
    private final String SECRET_KEY = "12345678901234567890123456789010";
    private final AuthenticationService service = new JwtService(new JwtUtils(SECRET_KEY));
    private final String validEmail = "qjawlsqjacks@naver.com";
    private final String validPassword = "1234";

    private Claims decodingResult(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("유효한 유저 정보가 주어지면")
        class Context_with_validUserData {
            private final UserLoginData validLoginData = new UserLoginData(validEmail, validPassword);

            @Test
            @DisplayName("토큰을 리턴한다")
            void It_returns_token() {
                String expectToken = service.login(validLoginData);

                assertThat(validLoginData.getEmail())
                        .isEqualTo(decodingResult(expectToken).get("email"));
            }
        }
    }
}
