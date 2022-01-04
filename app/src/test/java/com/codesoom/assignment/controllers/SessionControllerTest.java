package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.LoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SessionController 클래스")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthenticationService authenticationService;

    LoginData loginData;

    @BeforeEach
    void setUp() {
        loginData = LoginData.builder()
                .email("pjh0819@codesom.com")
                .password("123456")
                .build();
    }

    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_post {

        @Nested
        @DisplayName("LoginData가 주어진다면")
        class Context_with_loginData {

            @Test
            @DisplayName("access token을 응답합니다.")
            void login() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginDataToContent(loginData)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.accessToken").value(containsString(".")))
                        .andDo(print());
            }
        }
    }

    private String loginDataToContent(LoginData loginData) throws JsonProcessingException {
        return objectMapper.writeValueAsString(loginData);
    }
}
