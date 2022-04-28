package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789012";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login() {
        String accessToken = authenticationService.login();

        System.out.println(accessToken);

        assertThat(accessToken).contains(".");
    }
}