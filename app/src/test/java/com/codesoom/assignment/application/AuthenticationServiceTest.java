package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.LoginFailedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {

    private static final String EXISTED_EMAIL_ADDRESS = "existed@example.com";
    private static final String NOT_EXISTED_EMAIL_ADDRESS = "existed@example.com";

    private static final String SECRET_KEY = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    @Autowired
    private AuthenticationService authenticationService;

    private final JwtUtil jwtUtil = new JwtUtil(SECRET_KEY);

    private final UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void loginWithExistedEmailAndCorrectPassword() {
        String password = "abc1234";
        String token = "";

        if (userRepository.existsByEmail(EXISTED_EMAIL_ADDRESS)) {
            User existedUser = userRepository
                                .findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, password)
                                .get();

            token = jwtUtil.createToken(existedUser.getId());
        }

        verify(userRepository).existsByEmail(EXISTED_EMAIL_ADDRESS);
        verify(userRepository).findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, password);

        assertThat(token).isNotBlank();
        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    void loginWithNotExistedEmailThrowsUserNotFoundException() {
        given(userRepository.existsByEmail(NOT_EXISTED_EMAIL_ADDRESS))
                .willThrow(UserNotFoundException.class);

        assertThatThrownBy(() -> userRepository.existsByEmail(NOT_EXISTED_EMAIL_ADDRESS))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).existsByEmail(NOT_EXISTED_EMAIL_ADDRESS);
    }

    @Test
    void loginWithWrongPasswordThrowsLoginFailedException() {
        String wrongPassword = "wrongPassword";

        given(userRepository.findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, wrongPassword))
                .willThrow(new LoginFailedException(EXISTED_EMAIL_ADDRESS));

        assertThatThrownBy(() -> userRepository.findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, wrongPassword))
                .isInstanceOf(LoginFailedException.class);

        verify(userRepository).findByEmailAndPassword(EXISTED_EMAIL_ADDRESS, wrongPassword);
    }
}
