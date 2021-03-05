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

    private final Long parsedUserId = 1L;

    private final String secret = "12345678901234567890123456789012";
    private final long validTime = 604800000; // an hour

    private final String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private final String inValidToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDA";

    private final String email = "jamie@example.com";
    private final String password = "12345678";

    private User user;
    private User deletedUser;

    private UserLoginData userLoginData;
    private UserLoginData userLoginDataWithNotExistingEmail;
    private UserLoginData userLoginDataWithWrongPassword;


    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(secret, validTime);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        user = User.builder()
                .email(email)
                .password(password)
                .build();

        deletedUser = User.builder()
                .email(email)
                .password(password)
                .deleted(true)
                .build();

        userLoginData = UserLoginData.builder()
                .email(email)
                .password(password)
                .build();

        userLoginDataWithNotExistingEmail = UserLoginData.builder()
                .email(email + "_invalid_email")
                .password(password)
                .build();

        userLoginDataWithWrongPassword = UserLoginData.builder()
                .email(email)
                .password(password + "_wrong_password")
                .build();
    }

    @Nested
    @DisplayName("login")
    class Describe_login {
        @Nested
        @DisplayName("유효한 로그인 정보가 주어진다면")
        class Context_with_valid_user_login_data {
            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginData.getEmail()))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("액세스 토큰을 리턴한다.")
            @Test
            void it_returns_the_access_token() {
                final String accessToken = authenticationService.login(userLoginData);

                assertThat(accessToken).contains(".");
            }
        }

        @Nested
        @DisplayName("주어진 이메일에 해당하는 회원이 없다면")
        class Context_when_the_user_does_not_exist {
            private final String errorKeyword = "잘못된 이메일 주소";

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginDataWithNotExistingEmail.getEmail()))
                        .willReturn(Optional.empty());
            }

            @DisplayName("'회원 인증에 실패했습니다' 라는 예외를 던진다.")
            @Test
            void it_throws_user_authentication_fail_exception() {
                UserAuthenticationFailException e = assertThrows(UserAuthenticationFailException.class,
                        () -> authenticationService.login(userLoginDataWithNotExistingEmail));

                assertThat(e.getMessage()).contains(errorKeyword);
            }
        }

        @Nested
        @DisplayName("주어진 로그인 정보의 비밀번호가 잘못되었다면")
        class Context_when_the_password_is_wrong {
            private final String errorKeyword = "잘못된 비밀번호";

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginDataWithWrongPassword.getEmail()))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("'회원 인증에 실패했습니다' 라는 예외를 던진다.")
            @Test
            void it_throws_user_authentication_fail_exception() {
                UserAuthenticationFailException e = assertThrows(UserAuthenticationFailException.class,
                        () -> authenticationService.login(userLoginDataWithWrongPassword));

                assertThat(e.getMessage()).contains(errorKeyword);
            }
        }

        @Nested
        @DisplayName("주어진 로그인 정보에 해당하는 회원이 이미 삭제된 회원 이라면")
        class Context_when_the_user_is_already_deleted {
            private final String errorKeyword = "삭제된 회원";

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginData.getEmail()))
                        .willReturn(Optional.of(deletedUser));
            }

            @DisplayName("'회원 인증에 실패했습니다' 라는 예외를 던진다.")
            @Test
            void it_throws_user_authentication_fail_exception() {
                UserAuthenticationFailException e = assertThrows(UserAuthenticationFailException.class,
                        () -> authenticationService.login(userLoginData));

                assertThat(e.getMessage()).contains(errorKeyword);
            }
        }
    }

    @Nested
    @DisplayName("parseToken")
    class Describe_parseToken {
        @Nested
        @DisplayName("유효한 토큰이 주어진다면")
        class Context_with_a_valid_token {
            @DisplayName("파싱된 값을 리턴한다.")
            @Test
            void it_returns_the_parsed_value() {
                final Long userId = authenticationService.parseToken(validToken);

                assertThat(userId).isEqualTo(parsedUserId);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어진다면")
        class Context_with_a_invalid_token {
            @DisplayName("'토큰이 유효하지 않습니다.' 라는 예외를 던진다.")
            @Test
            void it_throws_invalid_token_exception() {
                assertThrows(InvalidTokenException.class,
                        () -> authenticationService.parseToken(inValidToken));
            }
        }
    }

}



