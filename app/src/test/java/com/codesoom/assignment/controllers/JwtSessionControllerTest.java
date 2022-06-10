package com.codesoom.assignment.controllers;


import com.codesoom.assignment.application.JwtAuthService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(JwtSessionController.class)
@DisplayName("JwtSessionController")
class JwtSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtAuthService authService;
    @MockBean
    private UserService userService;

    private final String EMAIL = "test@gmail.com";
    private final String EMAIL_NOT_EXISTING = "notfound@gmail.com";
    private final String PASSWORD = "test0012300";
    private final String PASSWORD_NOT_EQUAL = "no0012300";
    private final Long USER_ID = 1L;
    private final String JWT =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIxIn0.aqbG22EmNECI69ctM6Jsas4SWOxalVlcgF05iujelq4";

    private LoginRequestData requestData;

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {
        private User user;

        @Nested
        @DisplayName("동일한 비밀번호와 존재하는 이메일이 담긴 사용자 정보를 전달받으면")
        class Context_when_valid_auth_data {

            @BeforeEach
            void setUp() {
                user = User.builder()
                        .id(USER_ID)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
                requestData = LoginRequestData.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();

                given(userService.findBy(EMAIL)).willReturn(user);
                given(authService.login(requestData.getEmail(), requestData.getPassword())).willReturn(JWT);
            }

            @Test
            @DisplayName("HTTP Status Code 200을 응답한다")
            void it_responses_200_ok() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFrom(requestData)))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("JWT를 반환한다")
            void it_returns_jwt() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFrom(requestData)))
                        .andExpect(jsonPath("$.accessToken").value(JWT));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 사용자 이메일을 전달 받으면")
        class Context_with_not_existing_email {

            @BeforeEach
            void setUp() {
                requestData = LoginRequestData.builder()
                        .email(EMAIL_NOT_EXISTING)
                        .password(PASSWORD)
                        .build();

                given(authService.login(requestData.getEmail(), requestData.getPassword()))
                        .willThrow(new UserNotFoundException(EMAIL_NOT_EXISTING));
            }

            @Test
            @DisplayName("HTTP Status Code 404로 응답한다.")
            void it_responses_404_not_found() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonFrom(requestData)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    private String jsonFrom(LoginRequestData requestData) throws JsonProcessingException {
        return objectMapper.writeValueAsString(requestData);
    }
}
