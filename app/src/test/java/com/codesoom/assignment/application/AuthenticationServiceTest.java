package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AuthenticationService에 대한 테스트 클래스
 */
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private AuthenticationService authenticationService;

    private JwtUtil jwtUtil;
    private String token;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login_method {
        @BeforeEach
        void setUp() {
            token = authenticationService.login();
        }

        @Test
        @DisplayName("토큰을 반환한다")
        void It_returns_encoded_userId_string() {
            assertThat(token).isEqualTo(TOKEN);
        }
    }
}
