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
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("post 요청시")
    class Describe_post {
        @BeforeEach
        void setUp() {
            given(authenticationService.login()).willReturn("a.b.c");
        }

        @Nested
        @DisplayName("")
        class Context {
            @Test
            @DisplayName("요청이 성공적으로 처리되었음을 응답한다.")
            void it_status_created() throws Exception {
                mockMvc.perform(post("/session"))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(".")));

            }
        }
    }
}
