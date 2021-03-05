package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.AuthenticationFailException;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    private final Long parsedUserId = 1L;

    private final String SECRET = "12345678901234567890123456789010";

    private static final String VALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJ1c2VySWQiOjF9."
            + "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final String INVALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJ1c2VySWQiOjF9."
            + "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGG3";

    private static final String NULL_TOKEN = null;

    private final String USER_EMAIL = "olive@gamil.com";
    private final String USER_PASSWORD = "password1234";

    private User user;
    private UserLoginData userLoginData;
    private UserLoginData userLoginDataWithoutExistingEmail;
    private UserLoginData userLoginDataWithWrongPassword;


    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        user = User.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        userLoginData = UserLoginData.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        userLoginDataWithoutExistingEmail = UserLoginData.builder()
                .email("invalid" + USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        userLoginDataWithWrongPassword = UserLoginData.builder()
                .email(USER_EMAIL)
                .password("wrong" + USER_PASSWORD)
                .build();
    }

    @Nested
    @DisplayName("login은")
    class Describe_login {

        @Nested
        @DisplayName("유효한 로그인 정보가 주어면")
        class Context_with_valid_login_data {
            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginData.getEmail()))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("인증 토큰을 리턴한다.")
            @Test
            void it_returns_access_token() {
                String accessToken = authenticationService.login(userLoginData);

                assertThat(accessToken).contains(".");
            }
        }

        @Nested
        @DisplayName("등록되지 않은 이메일이 주어지면")
        class Context_with_invalid_login_data {

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginDataWithoutExistingEmail.getEmail()))
                        .willReturn(Optional.empty());
            }

            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_exception() {
                assertThatThrownBy(() -> authenticationService.login(userLoginDataWithoutExistingEmail))
                        .isInstanceOf(AuthenticationFailException.class);
            }
        }

        @Nested
        @DisplayName("주어진 로그인 정보의 비밀번호가 일치하지 않는다면")
        class Context_with_wrong_password {

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(userLoginDataWithWrongPassword.getEmail()))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_exception() {
                assertThatThrownBy(() -> authenticationService.login(userLoginDataWithWrongPassword))
                        .isInstanceOf(AuthenticationFailException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken은")
    class Describe_parseToken {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {

            @DisplayName("파싱된 값을 리턴한다.")
            @Test
            void it_returns_the_parsed_value() {
                Long userId = authenticationService.parseToken(VALID_TOKEN);

                assertThat(userId).isEqualTo(parsedUserId);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_invalid_token {

            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_exception() {
                assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                        .isInstanceOf(InvalidAccessTokenException.class);

            }
        }

        @Nested
        @DisplayName("값이 null이거나 비어있는 토큰이 주어지면")
        class Context_with_null_token {
            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_exception() {
                assertThatThrownBy(() -> authenticationService.parseToken(NULL_TOKEN));
            }
        }
    }
}
