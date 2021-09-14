package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;


class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    private static final String secret = "12345678901234567890123456789010";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(secret);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login() {
        String accessToken = authenticationService.login();
        assertThat(accessToken).contains(".");
    }

}