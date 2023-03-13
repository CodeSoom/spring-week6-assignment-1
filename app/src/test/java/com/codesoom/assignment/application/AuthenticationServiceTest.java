package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {

        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    @DisplayName("login")
    public void login() throws Exception {
        //given
        //when
        String accessToken = authenticationService.login();
        //Then
        assertThat(accessToken).contains(".xxx");
    }

}