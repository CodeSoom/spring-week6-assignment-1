package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.LoginRequestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5qbdTrYLuxHeNPvUdPmExYWv25gk7BwSjhAoPgoIvaA";


    @BeforeEach
    void setUp() {
        given(authenticationService.login(any(LoginRequestData.class))).willReturn(VALID_TOKEN);
    }

    @Test
    void loginWithValidAttributes() throws Exception {

        mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"email\":\"shinsanghooon@gmail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(VALID_TOKEN)))
                .andDo(print());

        verify(authenticationService).login(any(LoginRequestData.class));
    }

    @Test
    void loginWithInValidAttributes() throws Exception {

        mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}