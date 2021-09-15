package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String ACCESS_INVALID_TOKEN = ACCESS_TOKEN + "invalid";

    AuthenticationService authenticationService;
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {

        jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil);

    }

    @Test
    void login() {

        String accessToken = authenticationService.login();
        assertThat(accessToken).contains(".");

    }

    @Test
    void parse() {

        Long userId = authenticationService.parseToken(ACCESS_TOKEN);
        assertThat(userId).isEqualTo(1L);

    }

}

