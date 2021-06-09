package com.codesoom.assignment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp(){
        authenticationService = new AuthenticationService();
    }

    @Test
    public void login(){
        String accessToken = authenticationService.login();

        System.out.println("accessToken = " + accessToken);

        assertThat(accessToken).contains(".");
    }
}