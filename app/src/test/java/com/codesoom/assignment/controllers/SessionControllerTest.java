package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
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
@DisplayName("SessionController 클래스")
class SessionControllerTest {
    private static final String VALID_TOKEN = "a.b.c";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        given(authenticationService.login()).willReturn(VALID_TOKEN);
    }

    @Nested
    @DisplayName("POST /session은")
    class Describe_request_post_to_session_path {

        @Test
        @DisplayName("토큰을 응답합니다.")
        void it_responses_token() throws Exception {
            mockMvc.perform(post("/session"))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(VALID_TOKEN)));
        }
    }
}
