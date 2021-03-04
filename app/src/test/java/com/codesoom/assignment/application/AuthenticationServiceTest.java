package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    final String validToken
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    final String invalidToken
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    final String givenEmail = "newoo4297@codesoom.com";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        final String secret = "12345678901234567890123456789012";

        JwtUtil jwtUtil = new JwtUtil(secret);
        UserRepository userRepository = mock(UserRepository.class);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        given(userRepository.findByEmail(givenEmail))
                .willReturn(
                        Optional.of(User.builder()
                                .id(1L)
                                .email(givenEmail)
                                .name("Tester")
                                .password("test")
                                .build())
                );
    }

    @Test
    void login() {
        String accessToken = authenticationService.login(givenEmail);

        assertThat(accessToken).isEqualTo(validToken);
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(validToken);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(
                () -> authenticationService.parseToken(invalidToken)
        ).isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void parseTokenWithBlankToken() {
        assertThatThrownBy(
                () -> authenticationService.parseToken(null)
        ).isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(
                () -> authenticationService.parseToken("")
        ).isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(
                () -> authenticationService.parseToken(" ")
        ).isInstanceOf(InvalidAccessTokenException.class);
    }
}
