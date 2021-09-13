package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789010";

    AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {

        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);

    }


    @Test
    void login() {

        String accessToken = authenticationService.login();

        assertThat(accessToken).contains(".111");

    }

}