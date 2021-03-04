package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest {
    final String validToken
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    final String invalidToken
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        final String secret = "12345678901234567890123456789012";

        JwtUtil jwtUtil = new JwtUtil(secret);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login() {
        String accessToken = authenticationService.login();

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
