package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SessionController 클래스")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_post {

        @BeforeEach
        void setUp() {
            given(authenticationService.login()).willReturn("a.b.c");
        }

        @Test
        @DisplayName("access token을 응답합니다.")
        void login() throws Exception {
            mockMvc.perform(post("/session"))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(".")))
                    .andDo(print());
        }
    }
}
