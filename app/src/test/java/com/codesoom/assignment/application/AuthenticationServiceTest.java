package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserAuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private final UserRepository userRepository = mock(UserRepository.class);

    private final String EMAIL = "email@example.co";
    private final String PASSWORD = "password";

    private final String SECRET = "12345678901234567890123456789012";
    private final long VALID_TIME = 24 * 60 * 60 * 1000;

    private User user;
    private UserLoginData userLoginData;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, VALID_TIME);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        userLoginData = UserLoginData.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Nested
    @DisplayName("login")
    class Describe_login {

        @Nested
        @DisplayName("유효한 로그인 정보가 주어지면")
        class Context_with_valid_user_login_data {

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginData.getEmail()))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("액세스 토큰을 리턴한다.")
            @Test
            void it_returns_the_access_token() {
                final String ACCESS_TOKEN = authenticationService.login(userLoginData);

                assertThat(ACCESS_TOKEN).contains(".");
            }
        }

        @Nested
        @DisplayName("주어진 이메일에 해당하는 회원이 없다면")
        class Context_when_the_user_does_not_exist {
            private final String WRONG_EMAIL = "wrong email";

            UserLoginData wrongEmailUserLoginData = UserLoginData.builder()
                    .email(WRONG_EMAIL)
                    .password(PASSWORD)
                    .build();

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(wrongEmailUserLoginData.getEmail()))
                        .willReturn(Optional.empty());
            }

            @DisplayName("UserAuthenticationFailException 예외를 던진다.")
            @Test
            void it_throws_user_authentication_fail_exception() {
                assertThrows(UserAuthenticationFailException.class,
                        () -> authenticationService.login(wrongEmailUserLoginData));
            }
        }

        @Nested
        @DisplayName("주어진 비밀번호가 잘못되었다면")
        class Context_when_the_password_is_wrong {
            private final String WRONG_PASSWORD = "wrong password";

            UserLoginData wrongPasswordUserLoginData = UserLoginData.builder()
                    .email(EMAIL)
                    .password(WRONG_PASSWORD)
                    .build();

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(wrongPasswordUserLoginData.getEmail()))
                        .willReturn(Optional.empty());
            }

            @DisplayName("UserAuthenticationFailException 예외를 던진다.")
            @Test
            void it_throws_user_authentication_fail_exception() {
                assertThrows(UserAuthenticationFailException.class,
                        () -> authenticationService.login(wrongPasswordUserLoginData));
            }
        }
    }
}
