package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginFailException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationService authenticationService;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    public static final String VALID_EMAIL = "dh@gmail.com";
    private static final String VALID_PASSWORD = "1111";
    public static final String INVALID_EMAIL = "dh";
    private static final String INVALID_PASSWORD = "0000";
    private static final UserLoginData VALID_LOGIN_DATA = UserLoginData.builder()
            .email(VALID_EMAIL)
            .password(VALID_PASSWORD)
            .build();
    private static final UserLoginData INVALID_LOGIN_DATA = UserLoginData.builder()
            .email(INVALID_EMAIL)
            .password(INVALID_PASSWORD)
            .build();

    @Test
    void loginWithValidLoginData() throws Exception {
        given(authenticationService.login(any(UserLoginData.class)))
                .willReturn(VALID_TOKEN);

        mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(VALID_LOGIN_DATA)))
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(VALID_TOKEN)));

        verify(authenticationService).login(any(UserLoginData.class));
    }

    @Test
    void loginWithEmptyLoginData() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UserLoginData.builder()
                                        .email("")
                                        .password("")
                                        .build()
                        )))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void loginWithInvalidLoginData() throws Exception {
        given(authenticationService.login(any(UserLoginData.class)))
                .willThrow(new LoginFailException());

        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(INVALID_LOGIN_DATA)))
                .andExpect(status().isUnauthorized());

        verify(authenticationService).login(any(UserLoginData.class));
    }

}
