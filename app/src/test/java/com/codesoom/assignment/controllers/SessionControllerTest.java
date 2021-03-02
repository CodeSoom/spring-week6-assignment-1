package com.codesoom.assignment.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {
    private MockMvc mockMvc;

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/session"))
            .andExpect(
                status().isCreated()
            )
            .andExpect(
                content().string(containsString("."))
            );
    }
}
