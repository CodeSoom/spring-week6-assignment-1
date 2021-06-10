package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "sample@google.com";
    private static final String PASSWORD = "sample_password";
    private static final String NAME = "sample";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        LoginData loginData = LoginData.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        given(userService.findUserByEmailPassword(any(LoginData.class)))
                .willReturn(User.builder()
                        .id(ID)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .name(NAME)
                        .build());

        given(authenticationService.login(ID)).willReturn("aaa.bbb.ccc");
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/session")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(String.format("{\"email\":\"%s\"," +
                            "\"password\":\"%s\"}", EMAIL, PASSWORD)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }
}
