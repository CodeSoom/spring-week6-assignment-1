package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.InvalidUserInformationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setSessionController() {
        String validUserEmail = "las@magical";
        String validUserPassword = "qwerty";
        String invalidUserEmail = "aaa@bbb.ccc";
        String invalidUserPassword = "invalid password";

        User validUser = User.builder()
            .id(1L)
            .name("TEST")
            .password(validUserPassword)
            .email(validUserEmail)
            .build();
        given(authenticationService.authenticate(validUserEmail, validUserPassword))
            .willReturn(validUser);
        given(authenticationService.authenticate(invalidUserEmail, invalidUserPassword))
            .willThrow(InvalidUserInformationException.class);

        given(authenticationService.issueToken(validUser)).willReturn("{\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.1KP0SsvENi7Uz1oQc07aXTL7kpQG5jBNIybqr60AlD4\"}");
    }

    @Test
    void loginWithValidUser() throws Exception {
        String validUserJson = "{\"email\":\"las@magical\", \"password\": \"qwerty\"}";
        mockMvc.perform(
            post("/session")
                .content(validUserJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status().isCreated()
            )
            .andExpect(
                content().string(containsString("."))
            );
    }

    @Test
    void loginWithInvalidUser() throws Exception {
        String validUserJson = "{\"email\":\"aaa@bbb.ccc\", \"password\": \"invalid password\"}";
        mockMvc.perform(
            post("/session")
                .content(validUserJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status().isBadRequest()
            );
    }
}
