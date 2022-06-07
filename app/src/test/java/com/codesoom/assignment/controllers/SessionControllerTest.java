package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

            @BeforeEach
            void setUp() {
                LoginResult loginResult = new LoginResult(TOKEN);

                given(authService.login(any(LoginData.class)))
                        .willReturn(loginResult);
            }

            @Test
            @DisplayName("인증 토큰이 담긴 LoginResult를 반환한다.")
            void it_returns_login_result() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"email\": \"kimchi@naver.com\"," +
                                                " \"password\": \"12345678\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.accessToken").value(TOKEN));
            }

        }
    }

}