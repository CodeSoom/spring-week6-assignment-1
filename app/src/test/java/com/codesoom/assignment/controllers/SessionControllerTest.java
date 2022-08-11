package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SessionControllerTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        final JwtUtil jwtUtil = new JwtUtil(SECRET);
        final AuthenticationService authenticationService = new AuthenticationService(jwtUtil);
        final SessionController controller = new SessionController(authenticationService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Nested
    @DisplayName("POST /session은")
    class Describe_postSession {
        @Nested
        @DisplayName("등록되어 있는 유저 정보를 담아서 요청하면")
        class Context_withExistingUser {
            private SessionRegistrationData sessionRegistrationData;

            @BeforeEach
            void prepare() {
                // TODO: 시스템에 유저 등록
                sessionRegistrationData = new SessionRegistrationData("tester@test.com", "password");
            }

            @Test
            @DisplayName("Created Status, 유저 정보에 대한 토큰을 반환한다")
            void login() throws Exception {
                mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(sessionRegistrationData))
                        )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString("."))); // TODO: 실제 토큰으로 변경
            }
        }
    }
}
