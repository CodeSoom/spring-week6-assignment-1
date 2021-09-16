package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthenticationService authenticationService;

    private static final String VALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJ1c2VySWQiOjF9."
            + "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final String INVALID_TOKEN
            = VALID_TOKEN + "INVALID_TOKEN";

    private UserLoginData validLoginData;

    private final String EMAIL = "email@nohriter.som";
    private final String PASSWORD = "password";

    @BeforeEach
    void setUp() {
        given(authenticationService.login(any(UserLoginData.class))).willReturn(VALID_TOKEN);

        validLoginData = UserLoginData.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(content().string(containsString(VALID_TOKEN)));
    }

}
