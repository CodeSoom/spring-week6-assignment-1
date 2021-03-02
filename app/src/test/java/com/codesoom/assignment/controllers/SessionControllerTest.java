package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
1. createToken
 */
@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/session"))
                .andExpect(status().isCreated());
    }
}
