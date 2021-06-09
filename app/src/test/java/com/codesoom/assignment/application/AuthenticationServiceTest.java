package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
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
@DisplayName("AuthenticationService의")
class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private JwtUtil jwtUtil;

    private AuthenticationService authenticationService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        this.jwtUtil = new JwtUtil(SECRET);
        this.userService = mock(UserService.class);
        this.authenticationService = new AuthenticationService(jwtUtil, this.userService);
    }

    @Nested
    @DisplayName("login 메소드는")
    class DescribeLogin {

        private User user;

        @BeforeEach
        void userSetUp() {
            user = User.builder()
                    .name("Jack")
                    .email("jack@email.com")
                    .password("qwer1234")
                    .build();
        }

        @Nested
        @DisplayName("올바른 유저 인증 정보를 받았을 때")
        class ContextWithValidUserInfo {

            @BeforeEach
            void setUp() {
                given(userService.getUser(user.getEmail())).willReturn(user);
            }

            @Test
            @DisplayName("토큰을 반환합니다")
            void ItReturnsToken() {
                assertThat(authenticationService.login(user.getEmail(), user.getPassword()))
                        .isEqualTo(jwtUtil.encode(user.getId()));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 유저 정보를 받았을 때")
        class ContextWithNotExistedUser {

            @BeforeEach
            void setUp() {
                given(userService.getUser(user.getEmail()))
                        .willThrow(UserNotFoundException.class);
            }

            @Test
            @DisplayName("UserNotFoundException을 던집니다")
            void ItThrowsUserNotFoundException() {
                assertThatThrownBy(() -> authenticationService.login(user.getEmail(), user.getPassword()))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("틀린 비밀번호를 받았을 때")
        class ContextWithWrongPassword {

            @BeforeEach
            void setUp() {
                given(userService.getUser(user.getEmail()))
                        .willReturn(user);
            }

            @Test
            @DisplayName("WrongPasswordException을 던집니다")
            void ItThrowsWrongPasswordException() {
                assertThatThrownBy(() -> authenticationService.login(user.getEmail(), user.getPassword()+"as"))
                        .isInstanceOf(WrongPasswordException.class);
            }
        }
    }
}
