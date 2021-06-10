package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private static final String secret = "1234567890123456789012234567890123456789012234567890123456789012234567890123456789012";

    @BeforeEach
    public void setUp(){
        JwtUtil jwtUtil = new JwtUtil(secret);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    public void login(){
        String accessToken = authenticationService.login();

        System.out.println("accessToken = " + accessToken);

        assertThat(accessToken).contains(".");
    }
}