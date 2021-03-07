package com.codesoom.assignment.controllers;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.errors.LoginFailException;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        given(authenticationService.login("tester@example.com","test"))
            .willReturn("a.b.c");

        given(authenticationService.login("badguy@example.com","test"))
            .willThrow(new LoginFailException("badguy@example.com"));

        given(authenticationService.login("tester@example.com","xxx"))
            .willThrow(new LoginFailException("badguy@example.com"));
    }

    @DisplayName("올바른 정보로 로그인을 요청하면, 상태코드 201을 반환한다.")
    @Test
    void loginWithRightEmailAndPassword() throws Exception {

        mockMvc.perform(post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"tester@example.com\","
                + " \"password\":\"test\"}"))
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("")));
    }

    @DisplayName("올바르지 않은 이메일로 로그인을 요청하면, 상태코드 400을 리턴한다.")
    @Test
    void loginWithWrongEmail() throws Exception {

        mockMvc.perform(post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"badguy@example.com\","
                + " \"password\":\"test\"}"))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("올바르지 않은 패스워드로 로그인을 요청하면, 상태코드 400을 리턴한다.")
    @Test
    void loginWithWrongPassword() throws Exception {

        mockMvc.perform(post("/session")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"tester@example.com\","
                + " \"password\":\"xxx\"}"))
            .andExpect(status().isBadRequest());
    }
}
