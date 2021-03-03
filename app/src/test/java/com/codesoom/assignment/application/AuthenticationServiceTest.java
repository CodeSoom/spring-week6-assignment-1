package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * AuthenticationService 클래스
 */
class AuthenticationServiceTest {

    private final String SECRET = "12345678901234567890123456789012";

    private AuthenticationService authenticationService;

    private UserService userService = mock(UserService.class);
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, userService);
    }

    @Test
    @DisplayName("login()는 유효한 사용자 로그인 정보가 주어지면 생성된 토큰을 리턴한다")
    void login() {

        authenticationService.login();

    }
}
