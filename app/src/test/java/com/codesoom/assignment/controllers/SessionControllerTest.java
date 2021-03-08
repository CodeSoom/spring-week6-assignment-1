package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.AccountData;
import com.codesoom.assignment.errors.FailedAuthenticationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    final String validToken
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void loginWithValidAccountData() throws Exception {
        given(authenticationService.login(any(AccountData.class)))
                .willReturn(validToken);

        mockMvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"newoo4297@codesoom.com\",\"password\":\"1234567890\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(validToken)));
    }

    @Test
    void loginWithUnsavedEmail() throws Exception {
        given(authenticationService.login(any(AccountData.class)))
                .willThrow(FailedAuthenticationException.class);

        mockMvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"law@codesoom.com\",\"password\":\"1234567890\"}")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWithInvalidPassword() throws Exception {
        given(authenticationService.login(any(AccountData.class)))
                .willThrow(FailedAuthenticationException.class);

        mockMvc.perform(
                post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"newoo4297@codesoom.com\",\"password\":\"1234567891\"}")
        )
                .andExpect(status().isBadRequest());
    }
}
