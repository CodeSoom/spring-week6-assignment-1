package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static com.codesoom.assignment.utils.JwtUtilTest.VALID_TOKEN;
import static com.codesoom.assignment.utils.JwtUtilTest.INVALID_TOKEN;

import com.codesoom.assignment.utils.JwtUtil;
import com.codesoom.assignment.utils.JwtUtilTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationServiceTest {
    private AuthenticationService authenticationService;

    private static final String SECRET = "01234567890123456789012345678912";

    @BeforeEach
    void beforeEach() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login() {
        String accessToken = authenticationService.login();

        assertThat(accessToken).contains(".");
    }

    @Test
    void parseToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

}
