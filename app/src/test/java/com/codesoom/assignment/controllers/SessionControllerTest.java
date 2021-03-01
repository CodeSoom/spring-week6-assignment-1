package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("/users")
class SessionControllerTest {
    private final String givenEmail = "juuni.ni.i@gmail.com";
    private final String givenPassword = "secret";

    @Autowired
    private MockMvc mockMvc;

    private String generateSignInJSON(String email, String password) {
        return String.format(
                "{\"email\":\"%s\"," +
                        "\"password\":\"%s\"}",
                email, password
        );
    }

    @Nested
    @DisplayName("[POST] /users 요청은")
    class Describe_post_root {
        @Nested
        @DisplayName("주어진 데이터와 일치하는 저장된 유저가 있을 때")
        class Context_with_exists_user_correspond_given_data {
            @Test
            @DisplayName("status ok 응답과 함께 토큰을 응답한다.")
            void It_respond_status_ok_and_token() throws Exception {
                mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(generateSignInJSON(givenEmail, givenPassword))
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString(".")));
            }
        }
    }
}
