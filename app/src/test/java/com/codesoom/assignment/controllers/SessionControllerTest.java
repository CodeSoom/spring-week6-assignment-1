package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserAuthenticationFailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SessionController 클래스")
@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private UserLoginData validUserLoginData;
    private UserLoginData invalidUserLoginData;

    @BeforeEach
    void setUp() {
        validUserLoginData = UserLoginData.builder()
                .email("jamie@example.com")
                .password("12345678")
                .build();

        invalidUserLoginData = UserLoginData.builder()
                .email("invalidEmail@example.com")
                .password("invalidPassword")
                .build();
    }

    @Nested
    @DisplayName("POST 요청은")
    class Describe_POST {
        @Nested
        @DisplayName("유효한 회원 로그인 정보가 주어진다면")
        class Context_with_valid_user_login_data {
            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(UserLoginData.class)))
                        .willReturn("a.b.c");
            }

            @DisplayName("액세스 토큰과 상태코드 201을 응답한다.")
            @Test
            void it_responds_the_access_token_and_status_code_201() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserLoginData)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").exists())
                        .andExpect(content().string(containsString(".")));
            }
        }

        @Nested
        @DisplayName("유효하지 않는 회원 로그인 정보가 주어진다면")
        class Context_with_invalid_user_login_data {
            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(UserLoginData.class)))
                        .willThrow(new UserAuthenticationFailException());
            }

            @DisplayName("에러 메시지와 상태코드 400을 응답한다.")
            @Test
            void it_responds_the_error_message_and_status_code_400() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserLoginData)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("accessToken").doesNotExist())
                        .andExpect(jsonPath("message").exists());
            }
        }
    }

}
