package com.codesoom.assignment.application;

import com.codesoom.assignment.utills.JwtUtill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 권한 서비스")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private AuthenticationService authenticationSrvice;

    @BeforeEach
    void setUp() {
        JwtUtill jwtUtil = new JwtUtill(SECRET);
        authenticationSrvice = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("로그인 메소드는")
    class Decribe_login {
        @Nested
        @DisplayName("로그인 요청자를 로그인 처리하고")
        class Context {
            @Test
            @DisplayName("엑세스 토큰을 리턴한다.")
            void it_return() {
                String accessToken = authenticationSrvice.login();

                assertThat(accessToken).contains(".");
            }
        }
    }
}
