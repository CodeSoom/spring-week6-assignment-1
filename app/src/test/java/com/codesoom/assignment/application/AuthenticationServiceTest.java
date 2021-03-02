package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserAuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    private final String secret = "12345678901234567890123456789012";
    private final String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private final String inValidToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDA";

    private static final String GIVEN_VALID_EMAIL = "jamie@example.com";
    private static final String GIVEN_VALID_PASSWORD = "12345678";
    private static final String GIVEN_INVALID_EMAIL = "invalidEmail@example.com";
    private static final String GIVEN_INVALID_PASSWORD = "invalidPassword";

    private static final User user = User.builder()
            .email(GIVEN_VALID_EMAIL)
            .password(GIVEN_VALID_PASSWORD)
            .build();

    private static final UserLoginData userLoginData = UserLoginData.builder()
            .email(GIVEN_VALID_EMAIL)
            .password(GIVEN_VALID_PASSWORD)
            .build();

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(secret);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);
    }

    @DisplayName("login 메소드에 유효한 회원 정보가 주어진다면 생성된 액세스 토큰을 리턴한다.")
    @Test
    void loginWithValidUserLoginData() {
        given(userRepository.findByEmail(any()))
                .willReturn(Optional.of(user));

        String accessToken = authenticationService.login(userLoginData);

        assertThat(accessToken).contains(".");
    }

    @DisplayName("login 메소드에 유효하지 않은 회원 정보가 주어진다면 '회원 인증에 실패했습니다' 라는 예외를 던진다.")
    @ParameterizedTest(name = "{index} {3}")
    @MethodSource("provideInvalidUser")
    void loginWithInvalidUserLoginData(
            UserLoginData invalidUserLoginData,
            Optional<User> invalidUser,
            String errorKeyword,
            String representation) {
        given(userRepository.findByEmail(invalidUserLoginData.getEmail()))
                .willReturn(invalidUser);

        UserAuthenticationFailException exception = assertThrows(UserAuthenticationFailException.class,
                () -> authenticationService.login(invalidUserLoginData));

        assertThat(exception.getMessage()).contains(errorKeyword);
    }

    @DisplayName("parseToken 메소드는 유효한 토큰이 주어진다면 파싱된 값을 리턴한다.")
    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(validToken);

        assertThat(userId).isEqualTo(1L);
    }

    @DisplayName("parseToken 메소드는 유효하지 않은 토큰이 주어진다면 '토큰이 유효하지 않습니다.' 라는 예외를 던진다.")
    @Test
    void parseTokenWithInvalidToken() {
        assertThrows(InvalidTokenException.class,
                () -> authenticationService.parseToken(inValidToken));
    }

    private static Stream<Arguments> provideInvalidUser() {
        UserLoginData userLoginDataWithNotExistingEmail = UserLoginData.builder()
                .email(GIVEN_INVALID_EMAIL)
                .password(GIVEN_VALID_PASSWORD)
                .build();

        UserLoginData userLoginDataWithWrongPassword = UserLoginData.builder()
                .email(GIVEN_VALID_EMAIL)
                .password(GIVEN_INVALID_PASSWORD)
                .build();

        User deletedUser = User.builder()
                .email(GIVEN_VALID_EMAIL)
                .password(GIVEN_VALID_PASSWORD)
                .deleted(true)
                .build();

        return Stream.of(
                Arguments.of(
                        userLoginDataWithNotExistingEmail,
                        Optional.empty(),
                        "이메일",
                        "이메일이 존재하지 않을 경우"
                ),
                Arguments.of(
                        userLoginDataWithWrongPassword,
                        Optional.of(user),
                        "비밀번호",
                        "비밀번호가 일치하지 않을 경우"
                ),
                Arguments.of(
                        userLoginData,
                        Optional.of(deletedUser),
                        "삭제된",
                        "삭제된 회원일 경우"
                )
        );
    }

}
