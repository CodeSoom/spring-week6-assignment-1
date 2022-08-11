package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/session"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }
}
