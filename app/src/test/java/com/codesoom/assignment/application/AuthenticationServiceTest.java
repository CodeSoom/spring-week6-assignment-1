package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private static String secret = "123456789012345678901234567890123456789012";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(secret);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login() {
        SessionResponseData sessionResponseData = authenticationService.login(1L);
        assertThat(sessionResponseData.getAccessToken()).isEqualTo("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.4VSrshpJYgISpBrS2VeEk_uwz_djqdjBbcbaT0yfE9M");
    }
}
