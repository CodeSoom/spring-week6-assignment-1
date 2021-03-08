package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.AuthenticationFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String VALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJ1c2VySWQiOjF9."
            + "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private UserLoginData validLoginData;
    private UserLoginData invalidLoginData;

    @BeforeEach
    void setUp() {
        validLoginData = UserLoginData.builder()
                .email("olive@gmail.com")
                .password("password1234")
                .build();

        invalidLoginData = UserLoginData.builder()
                .email("invalid@invalid.commm")
                .password("invalidPassword")
                .build();
    }

    @Nested
    @DisplayName("POST 요청은")
    class Describe_POST {
        @Nested
        @DisplayName("로그인 인증에 성공하면")
        class Context_with_success_authentication {
            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(UserLoginData.class)))
                        .willReturn(VALID_TOKEN);
            }

            @DisplayName("201코드와 토큰을 응답한다.")
            @Test
            void it_responds_201_and_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginData)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").exists())
                        .andExpect(content().string(containsString(VALID_TOKEN)));
            }
        }

        @Nested
        @DisplayName("로그인 인증에 실패하면")
        class Context_with_fail_authentication {
            void setUp() {
                given(authenticationService.login(invalidLoginData))
                        .willThrow(new AuthenticationFailException("입력하신 로그인 정보가 맞지 않습니다."));
            }

            @Test
            @DisplayName("400 코드를 응답한다.")
            void it_responds_400() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("accessToken").doesNotExist());
            }
        }
    }
}
