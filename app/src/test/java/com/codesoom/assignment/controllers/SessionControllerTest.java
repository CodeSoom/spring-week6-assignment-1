package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Nested
@DisplayName("SessionController의")
public class SessionControllerTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private JwtUtil jwtUtil;

    private SessionController sessionController;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        this.authenticationService = mock(AuthenticationService.class);
        this.sessionController = new SessionController(this.authenticationService);
        this.jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("login 메소드는")
    class DescribeLogin {

        private User user;
        private LoginData loginData;

        @BeforeEach
        void userSetUp() {
            user = User.builder()
                    .name("Jack")
                    .email("jack@email.com")
                    .password("qwer1234")
                    .build();
            loginData = LoginData.builder()
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
        }

        @Nested
        @DisplayName("올바른 로그인 정보를 받았을 때")
        class ContextWithValidLoginData {

            private String token;

            @BeforeEach
            void setUp() {
                token = jwtUtil.encode(user.getId());
                given(authenticationService.login(user.getEmail(), user.getPassword()))
                        .willReturn(token);
            }

            @Test
            @DisplayName("토큰을 반환합니다")
            void ItReturnsToken() {
                assertThat(sessionController.login(loginData).getAccessToken())
                        .isEqualTo(token);
            }
        }

        @Nested
        @DisplayName("올바르지 않은 로그인 정보를 받으면")
        class ContextWithNotExistedUser {

            @BeforeEach
            void setUp() {
                given(authenticationService.login(user.getEmail(), user.getPassword()))
                        .willThrow(UserNotFoundException.class);
            }

            @Test
            @DisplayName("UserNotFoundException을 던집니다")
            void ItThrowsUserNotFoundException() {
                assertThatThrownBy(() -> sessionController.login(loginData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("틀린 비밀번호를 받으면")
        class ContextWithWrongPassword {

            @BeforeEach
            void setUp() {
                given(authenticationService.login(user.getEmail(), user.getPassword()))
                        .willThrow(WrongPasswordException.class);
            }

            @Test
            @DisplayName("WrongPassword를 던집니다")
            void ItReturnsWrongPassword() {
                assertThatThrownBy(() -> sessionController.login(loginData))
                        .isInstanceOf(WrongPasswordException.class);
            }
        }
    }
}
