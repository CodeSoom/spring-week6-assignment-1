package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserAuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    private final String secret = "12345678901234567890123456789012";

    private AuthenticationService authenticationService;

    private UserService userService = mock(UserService.class);

    private User user;
    private UserLoginData validUserLoginData;
    private UserLoginData inValidUserLoginData;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(secret);

        authenticationService = new AuthenticationService(jwtUtil, userService);

        user = User.builder()
                .id(1L)
                .email("jamie@example.com")
                .name("Jamie")
                .password("12345678")
                .build();


        validUserLoginData = UserLoginData.builder()
                .email("jamie@example.com")
                .password("12345678")
                .build();

        inValidUserLoginData = UserLoginData.builder()
                .email("invalidEmail@example.com")
                .password("invalidPassword")
                .build();
    }

    @DisplayName("유효한 회원 정보가 주어진다면 생성된 액세스 토큰을 리턴한다.")
    @Test
    void loginWithValidUserLoginData() {
        given(userService.findUserByEmail(any()))
                .willReturn(user);

        String accessToken = authenticationService.login(validUserLoginData);

        assertThat(accessToken).contains(".");
    }

    @DisplayName("유효하지 않은 회원 정보가 주어진다면 '회원 인증에 실패했습니다' 라는 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("provideInvalidUser")
    void loginWithInvalidUserLoginData(User invalidUser) {
        given(userService.findUserByEmail(any()))
                .willReturn(invalidUser);

        assertThrows(UserAuthenticationFailException.class,
                () -> authenticationService.login(inValidUserLoginData));
    }

    private static Stream<Arguments> provideInvalidUser() {
        User deletedUser = User.builder()
                .id(2L)
                .email("jamie@example.com")
                .name("Jamie")
                .password("12345678")
                .deleted(true)
                .build();

        User UserWithWrongPassword = User.builder()
                .id(3L)
                .email("jamie@example.com")
                .name("Jamie")
                .password("wrongPassword")
                .build();

        return Stream.of(
                Arguments.of(deletedUser),
                Arguments.of(UserWithWrongPassword)
        );
    }
}
