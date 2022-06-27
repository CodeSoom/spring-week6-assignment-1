package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
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

    private final Long PARSED_USER_ID = 1L;
    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsIml" +
            "hdCI6MTY1NDc1NzIxNSwiZXhwIjoxNjU0NzYwODE1fQ.NI_o2dC-go7A_IEJ34LOZPw5Ohg_KcVa48UUHDsKUpl";

    private final String EMAIL = "email@example.co";
    private final String PASSWORD = "password";

    private final String SECRET = "12345678901234567890123456789012";
    private final long VALID_TIME = 24 * 60 * 60 * 1000;

    private User user;
    private UserLoginData userLoginData;

    private JwtUtil jwtUtil = new JwtUtil(SECRET, VALID_TIME);

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, VALID_TIME);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        user = User.builder()
                .id(1L)
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
                String ACCESS_TOKEN = authenticationService.login(userLoginData);

                assertThat(authenticationService.parseToken(ACCESS_TOKEN)).isEqualTo(PARSED_USER_ID);
            }
        }

        @Nested
        @DisplayName("주어진 이메일에 해당하는 회원이 없다면")
        class Context_when_the_user_does_not_exist {
            private final String WRONG_EMAIL = "wrong email";
            private final String ERROR_KEYWORD = "잘못된 이메일 주소";

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
                UserAuthenticationFailException e = assertThrows(UserAuthenticationFailException.class,
                        () -> authenticationService.login(wrongEmailUserLoginData));

                assertThat(e.getMessage()).contains(ERROR_KEYWORD);
            }
        }

        @Nested
        @DisplayName("주어진 비밀번호가 잘못되었다면")
        class Context_when_the_password_is_wrong {
            private final String WRONG_PASSWORD = "wrong password";
            private final String ERROR_KEYWORD = "잘못된 비밀번호";

            UserLoginData wrongPasswordUserLoginData = UserLoginData.builder()
                    .email(EMAIL)
                    .password(WRONG_PASSWORD)
                    .build();

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(wrongPasswordUserLoginData.getEmail()))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("UserAuthenticationFailException 예외를 던진다.")
            @Test
            void it_throws_user_authentication_fail_exception() {
                UserAuthenticationFailException e = assertThrows(UserAuthenticationFailException.class,
                        () -> authenticationService.login(wrongPasswordUserLoginData));

                assertThat(e.getMessage()).contains(ERROR_KEYWORD);
            }
        }
    }

    @Nested
    @DisplayName("parseToken")
    class Describe_parseToken {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {

            @DisplayName("파싱한 값을 리턴한다.")
            @Test
            void it_returns_parsed_value() {
                final Long userId = authenticationService.parseToken(VALID_TOKEN);
                assertThat(userId).isEqualTo(PARSED_USER_ID);
            }

            @Nested
            @DisplayName("유효하지 않은 토큰이 주어지면")
            class Context_with_invalid_token {

                @DisplayName("InvalidTokenException을 던진다.")
                @Test
                void it_throws_invalid_token_exception() {
                    assertThrows(InvalidTokenException.class,
                            () -> authenticationService.parseToken(INVALID_TOKEN));
                }
            }
        }
    }
}
