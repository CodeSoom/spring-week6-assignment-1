package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginFailedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {

    private static final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
    private static final String NOT_EXISTED_EMAIL_ADDRESS = "notexisted@example.com";

    private static final String SECRET_KEY = "12345678901234567890123456789010";

    private AuthenticationService authenticationService;

    private final UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET_KEY);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        User user = User.builder()
                .id(1L)
                .email(EXISTED_EMAIL_ADDRESS)
                .name("kim")
                .password("abc1234")
                .build();

        given(userRepository.findByIdAndDeletedIsFalse(1L))
                .willReturn(Optional.of(user));

        given(userRepository.findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, "abc1234"))
                .willReturn(Optional.of(user));

        given(userRepository.existsByEmail(EXISTED_EMAIL_ADDRESS))
                .willReturn(Boolean.TRUE);

        given(userRepository.existsByEmail(NOT_EXISTED_EMAIL_ADDRESS))
                .willReturn(Boolean.FALSE);

        given(userRepository.findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, "wrongPassword"))
                .willThrow(new LoginFailedException(EXISTED_EMAIL_ADDRESS));
    }

    @Test
    void loginWithExistedEmailAndCorrectPassword() {
        UserLoginData userLoginData = UserLoginData.builder()
                                                    .email(EXISTED_EMAIL_ADDRESS)
                                                    .password("abc1234")
                                                    .build();

        authenticationService.login(userLoginData);

        verify(userRepository).findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, "abc1234");
    }

    @Test
    void loginWithNotExistedEmailThrowsUserNotFoundException() {
        UserLoginData userLoginData = UserLoginData.builder()
                .email(NOT_EXISTED_EMAIL_ADDRESS)
                .password("abc1234")
                .build();

        assertThatThrownBy(() -> authenticationService.login(userLoginData))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).existsByEmail(NOT_EXISTED_EMAIL_ADDRESS);
    }

    @Test
    void loginWithWrongPasswordThrowsLoginFailedException() {
        UserLoginData userLoginData = UserLoginData.builder()
                .email(EXISTED_EMAIL_ADDRESS)
                .password("wrongPassword")
                .build();

        assertThatThrownBy(() -> authenticationService.login(userLoginData))
                .isInstanceOf(LoginFailedException.class);

        verify(userRepository).findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, "wrongPassword");
    }
}
