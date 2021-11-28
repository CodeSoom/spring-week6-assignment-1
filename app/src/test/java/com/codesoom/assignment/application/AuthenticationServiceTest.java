package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJ1c2VySWQiOjF9."
            + "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final String INVALID_TOKEN
            = VALID_TOKEN + "INVALID_TOKEN";

    private final String EMAIL = "email@nohriter.som";
    private final String PASSWORD = "password";

    private AuthenticationService authenticationService;
    private UserRepository userRepository;
    private UserLoginData validUserLoginData;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        validUserLoginData = UserLoginData.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Test
    void login() {
        String accessToken = authenticationService.login(validUserLoginData);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }
}
