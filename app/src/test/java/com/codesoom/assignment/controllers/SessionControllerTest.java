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
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @BeforeEach
    void setUp() {
        given(authenticationService.login()).willReturn(TOKEN);
    }

    @Nested
    @DisplayName("POST /session 요청 시")
    class Describe_post_session {
        @Nested
        @DisplayName("status code 201을 응답한다")
        class It_response_status_code_201 {
            ResultActions subject() throws Exception {
                return mockMvc.perform(post("/session"));
            }

            @Test
            void test() throws Exception {
                subject().andExpect(status().isCreated())
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(TOKEN)));
            }
        }
    }
}
