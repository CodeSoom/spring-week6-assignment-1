package com.codesoom.assignment.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {
    private AuthenticationService authenticationService;

    

    @Test
    void login() {
        String accessToken = authenticationService.login();

        assertThat(accessToken).contains(".");
    }


}