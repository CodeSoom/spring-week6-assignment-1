package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.EmailNotFoundException;
import com.codesoom.assignment.errors.WrongPasswordException;
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
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = VALID_TOKEN + "invalid";
    @Autowired
    ObjectMapper objectMapper;

    UserLoginData loginData;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("POST 요청은")
    class Describe_POST {
        @Nested
        @DisplayName("로그인 인증에 성공하면")
        class Context_with_success_authentication {
            @BeforeEach
            void setUp() {
                loginData = UserLoginData.builder()
                        .email("test@test.codesoom")
                        .password("password")
                        .build();
                given(authenticationService.createToken(any(UserLoginData.class))).willReturn(VALID_TOKEN);
            }


            @DisplayName("201코드와 토큰을 응답한다.")
            @Test
            void it_responds_201_and_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginData)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").exists())
                        .andExpect((ResultMatcher) content().string(containsString(VALID_TOKEN)));
            }
        }

        @Nested
        @DisplayName("잘못되 페스워드로 로그인할 때")
        class Context_with_fail_authentication {
            void setUp() {

                loginData = UserLoginData.builder()
                        .email("test@test.codesoom")
                        .password("wrongPassword")
                        .build();
                given(authenticationService.createToken(loginData))
                        .willThrow(new WrongPasswordException(loginData));
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

        @Nested
        @DisplayName("잘못된 이메일로 로그인할 때")
        class Context_with_email {
            void setUp() {

                loginData = UserLoginData.builder()
                        .email("worngEmail@test.codesoom")
                        .password("wrongPassword")
                        .build();
                given(authenticationService.createToken(loginData))
                        .willThrow(new EmailNotFoundException(loginData));
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
