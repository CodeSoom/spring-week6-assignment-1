package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.errors.LoginFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("Describe:")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        given(authenticationService.login("code@soom.com", "test")).willReturn("a.b.c");
        given(authenticationService.login("cocode@soom.com", "test")).willThrow(new LoginFailException(""));
        given(authenticationService.login("code@soom.com", "wrongPassword")).willThrow(new LoginFailException(""));
    }

    @Test
    void loginWithRightEmailAndPassword() throws Exception {

        mockMvc.perform(post("/session")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"code@soom.com\", \"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }


    @Test
    void loginWithWrongEmail() throws Exception {

        mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"cocode@soom.com\", \"password\":\"test\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWithWrongPassword() throws Exception {

        mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"code@soom.com\", \"password\":\"wrongPassword\"}"))
                .andExpect(status().isBadRequest());
    }

}
