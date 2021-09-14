package com.codesoom.assignment.application;

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
        String accessToken = authenticationService.login();
        // 테스트코드의 fail 을 활용한 꼼수 쓰기
        assertThat(accessToken).contains(".blakblajk");
    }




}