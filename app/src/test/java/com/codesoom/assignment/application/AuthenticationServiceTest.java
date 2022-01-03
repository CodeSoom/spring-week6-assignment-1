package com.codesoom.assignment.application;

import com.codesoom.assignment.utills.JwtUtill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 권한 서비스 클래스 ")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private AuthenticationService authenticationSrvice;

    @BeforeEach
    void setUp() {

        JwtUtill jwtUtil = new JwtUtill(SECRET);
        authenticationSrvice = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("login 메소드는 ")
    class Decribe_login {
        @Nested
        @DisplayName("")
        class Context {
            @Test
            @DisplayName("")
            void it_return() {
                String accessToken = authenticationSrvice.login();

                assertThat(accessToken).contains(".");
            }
        }
    }
}
