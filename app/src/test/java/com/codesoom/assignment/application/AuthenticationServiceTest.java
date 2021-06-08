package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.PasswordNotCorrectException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789012";
    private static final String EXISTED_EMAIL = "example@login.com";
    private static final String NOT_EXISTED_EMAIL = "not-existed@login.com";
    private static final String CORRECT_PASSWORD = "password";
    private static final String INCORRECT_PASSWORD = "incorrect";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJleGFtcGxlQ" +
                                               "GxvZ2luLmNvbSJ9.VC6A_h3VehY_zvmxNGtgdwCc6zeFRBcdKSkj2rNB93Y";

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    private User user;

    private UserLoginData userLoginData;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        user = User.builder()
                   .email(EXISTED_EMAIL)
                   .password(CORRECT_PASSWORD)
                   .build();
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("유효한 사용자 로그인 정보가 주어지면")
        class Context_with_valid_userLoginData {

            @BeforeEach
            void prepareUserLoginData() {
                userLoginData = UserLoginData.builder()
                                             .email(EXISTED_EMAIL)
                                             .password(CORRECT_PASSWORD)
                                             .build();

                given(userRepository.findByEmail(EXISTED_EMAIL)).willReturn(Optional.of(user));
            }

            @Test
            @DisplayName("Access Token을 리턴한다")
            void it_returns_access_token() {
                String accessToken = authenticationService.login(userLoginData);
                assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
                verify(userRepository).findByEmail(EXISTED_EMAIL);
            }
        }

        @Nested
        @DisplayName("만약 존재하지 않는 Email이 주어지면")
        class Context_with_not_existed_email {

            @BeforeEach
            void prepareNotExistedEmail() {
                userLoginData = UserLoginData.builder()
                                             .email(NOT_EXISTED_EMAIL)
                                             .password(CORRECT_PASSWORD)
                                             .build();

                given(userRepository.findByEmail(NOT_EXISTED_EMAIL)).willThrow(new UserNotFoundException(NOT_EXISTED_EMAIL));
            }

            @Test
            @DisplayName("UserNotFoundException 예외를 던진다")
            void it_returns_userNotFoundException() {
                assertThatThrownBy(() -> authenticationService.login(userLoginData)).isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("만약 틀린 password가 주어지면")
        class Context_with_incorrect_password {

            @BeforeEach
            void prepareIncorrectPassword() {
                userLoginData = UserLoginData.builder()
                                             .email(EXISTED_EMAIL)
                                             .password(INCORRECT_PASSWORD)
                                             .build();

                given(userRepository.findByEmail(EXISTED_EMAIL)).willReturn(Optional.of(user));
            }

            @Test
            @DisplayName("PasswordNotCorrectException 예외를 던진다")
            void it_returns_passwordNotCorrectException() {
                assertThatThrownBy(() -> authenticationService.login(userLoginData)).isInstanceOf(PasswordNotCorrectException.class);
            }
        }
    }
}
