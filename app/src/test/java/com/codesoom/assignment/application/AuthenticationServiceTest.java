package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;

    @BeforeEach
    void set_up() {
        jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {

        @Test
        @DisplayName("유효한 JWT를 리턴한다")
        void It_returns_jwt() {
            String accessToken = authenticationService.login();

            assertThat(accessToken).contains(".");
        }
    }
}
