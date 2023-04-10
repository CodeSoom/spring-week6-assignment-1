package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AuthController ")
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("login 메소드는")
    class describe_Login {

        @Nested
        @DisplayName("패스워드가 일치할 경우")
        class context_with_valid_password {

            @Test
            @DisplayName("accessToken 과 201을 응답한다. ")
            void it_returns_accessToken() throws Exception {
                mockMvc.perform(post("/login"))
                        .andExpect(status().isCreated());
            }
        }
    }
}
