package com.codesoom.assignment.application;

import com.codesoom.assignment.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login(){
        String accessToken = authenticationService.login();

        assertThat(accessToken).contains(".");
    }

}
