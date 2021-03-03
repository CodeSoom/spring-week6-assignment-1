package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginDetail;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * AuthenticationService 클래스
 */
class AuthenticationServiceTest {

    private final String SECRET = "12345678901234567890123456789012";

    private AuthenticationService authenticationService;

    private UserService userService = mock(UserService.class);
    private JwtUtil jwtUtil;

    private UserLoginDetail validUserLoginDetail;
    private UserLoginDetail invalidUserLoginDetail;
    private User user;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, userService);

        validUserLoginDetail = UserLoginDetail.builder()
                .email("min@gmail.com")
                .password("1q2w3e!")
                .build();

        invalidUserLoginDetail = UserLoginDetail.builder()
                .email("invalid@gmail.com")
                .password("invalid")
                .build();

        user = User.builder()
                .id(1L)
                .email("min@gmail.com")
                .name("Min")
                .password("1q2w3e!")
                .build();

    }

    @Test
    @DisplayName("login()는 유효한 사용자 로그인 정보가 주어지면 생성된 토큰을 리턴한다")
    void login() {

        given(userService.findUserByEmail(any()))
                .willReturn(user);

        String token = authenticationService.login(validUserLoginDetail);
        assertThat(token).contains(".");

    }
}
