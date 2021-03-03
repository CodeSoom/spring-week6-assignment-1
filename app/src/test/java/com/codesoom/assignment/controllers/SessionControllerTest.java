package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginWithValidUser() throws Exception {
        String validUserJson = "{\"email\":\"las@magical\", \"password\": \"qwerty\"}";
        mockMvc.perform(
            post("/session")
                .content(validUserJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status().isCreated()
            )
            .andExpect(
                content().string(containsString("."))
            );
    }

    @Test
    void loginWithInvalidUser() throws Exception {
        String validUserJson = "{\"email\":\"aaa@bbb.ccc\", \"password\": \"invalid password\"}";
        mockMvc.perform(
            post("/session")
                .content(validUserJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                status().isBadRequest()
            );
    }
}
