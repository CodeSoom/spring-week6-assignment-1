package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AuthenticationServiceTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = VALID_TOKEN + "invalid";
    private final static String SECRETE_KEY = "12345678901234567890123456789010";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRETE_KEY);
        authenticationService = new AuthenticationService(jwtUtil);
    }


    @Test
    void login() {

        String accessToken = authenticationService.login();
        System.out.println("accessToken = " + accessToken);
        assertThat(accessToken).contains("");
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parsetoken(VALID_TOKEN);
        System.out.println("userId = " + userId);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInValidToken() {

        assertThatThrownBy(
                () -> authenticationService.parsetoken(INVALID_TOKEN)
        ).isInstanceOf(UnauthorizedException.class);

    }

    @Test
    void parseTokenWithBlank() {

        assertThatThrownBy(
                () -> authenticationService.parsetoken(null)
        ).isInstanceOf(UnauthorizedException.class);


        assertThatThrownBy(
                () -> authenticationService.parsetoken("")
        ).isInstanceOf(UnauthorizedException.class);

    }
}
