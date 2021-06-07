package com.codesoom.assignment.application;

import com.codesoom.assignment.uitls.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private static final String SECRET = "gkskenfdutkeidktjeifnturldksiekt";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login() {
        String accessToken = authenticationService.login();

        assertThat(accessToken).contains(".xxx");
    }
}