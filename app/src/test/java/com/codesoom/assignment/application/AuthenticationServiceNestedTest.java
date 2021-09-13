package com.codesoom.assignment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceNestedTest {
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService();
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("회원의 아이디와 비밀번호가 일치한다면")
        class Context_with_valid_data {
            @DisplayName("정상적으로 로그인되어 토큰이 발급된다.")
            @Test
            void login() {

            }
        }

        @Nested
        @DisplayName("회원의 아이디와 비밀번호가 불일치한다면")
        class Context_with_invalid_data {
            @DisplayName("예외가 발생하며 토큰이 발급되지 않는다.")
            @Test
            void login() {

            }
        }
    }

}
