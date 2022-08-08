package com.codesoom.assignment.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthenticationController 클래스의")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("요청이 들어오면")
        class Context_with_request {
            @Test
            @DisplayName("토큰 값과 상태코드 201을 응답한다")
            void It_respond_token() throws Exception {
                mockMvc.perform(post("/session" + "/1"))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(".")));
            }
        }
    }
}
