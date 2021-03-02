package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService();
    }
    
    @Test
    void login() {
        User user = User.builder()
                .id(1L)
                .email("rhfpdk92@naver.com")
                .password("1234")
                .name("양승인")
                .build();
        String login = authenticationService.login(user);
        assertThat(login).contains(".");
    }
}
