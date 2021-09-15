package com.codesoom.assignment.session.controller;

import com.codesoom.assignment.session.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private final String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjF9.mD0HZddWR7ufVRC_RyhCe_uUnB1ZF3XYM5kgfKFdEACQpLjIRoIozX4WqGYtSLqaSGGMhz2s1hovSn3QcG2_Og";

    @BeforeEach
    void setUp() {
        given(authenticationService.login(1L)).willReturn("a.b.c");
    }

    @Nested
    @DisplayName("login 메서드는")
    class testLogin {

        @Test
        @DisplayName("토큰을 반환한다.")
        void login() throws Exception {
            mockMvc.perform(post("/session/1"))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(TOKEN));
        }
    }
}
