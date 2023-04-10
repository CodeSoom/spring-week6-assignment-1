package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.LoginRequestData;
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

@DisplayName("AuthenticationController ")
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {
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
                LoginRequestData loginRequestData = LoginRequestData.builder()
                        .email("test123@naver.com")
                        .password("test123")
                        .build();

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequestData.toString()))
                        .andExpect(content().string(containsString(".")))
                        .andExpect(status().isCreated());
            }
        }
    }
}
