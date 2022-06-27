package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import com.codesoom.assignment.errors.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private static final String TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIn0.aqbG22EmNECI69ctM6Jsas4SWOxalVlcgF05iujelq4";


    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_login {

        @Nested
        @DisplayName("유효한 email과 password가 들어있는 LoginData가 주어지면")
        class Context_when_valid_login_data {
            private final LoginData loginData = LoginData.builder()
                    .email("kimchi@naver.com")
                    .password("12345678")
                    .build();

            @BeforeEach
            void setUp() {
                LoginResult loginResult = new LoginResult(TOKEN);

                given(authService.login(loginData))
                        .willReturn(loginResult);
            }

            @Test
            @DisplayName("인증 토큰이 담긴 LoginResult를 응답한다.")
            void it_responses_login_result() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"email\": \"kimchi@naver.com\"," +
                                                " \"password\": \"12345678\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.accessToken").value(TOKEN));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 이메일이 주어지면")
        class Context_when_non_existed_email {
            private final LoginData loginData = LoginData.builder()
                    .email("nonexistedEmail@naver.com")
                    .password("12345678")
                    .build();

            @BeforeEach
            void setUp() {
                given(authService.login(loginData))
                        .willThrow(AuthenticationException.class);
            }

            @Test
            @DisplayName("404 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"email\": \"nonexistedEmail@naver.com\"," +
                                                " \"password\": \"12345678\"}"))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("틀린 password가 주어지면")
        class Context_when_bad_password {
            private final LoginData loginData = LoginData.builder()
                    .email("kimchi@naver.com")
                    .password("12333434")
                    .build();

            @BeforeEach
            void setUp() {
                given(authService.login(loginData))
                        .willThrow(AuthenticationException.class);
            }

            @Test
            @DisplayName("400 status를 응답한다.")
            void it_responses_404_status() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"email\": \"kimchi@naver.com\"," +
                                                " \"password\": \"12333434\"}"))
                        .andExpect(status().isBadRequest());
            }
        }
    }

}
