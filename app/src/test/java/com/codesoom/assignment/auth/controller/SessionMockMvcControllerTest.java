package com.codesoom.assignment.auth.controller;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.dto.LoginRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SessionController 클래스")
@WebMvcTest(SessionController.class)
class SessionMockMvcControllerTest {
    private static final String WRONG_DATA = "잘못된 정보를 입력하였습니다.";
    private static final String GIVEN_USER_EMAIL = "test@test.com";
    private static final String GIVEN_USER_PASSWORD = "password";
    private static final String NOT_EXIST_EMAIL = GIVEN_USER_EMAIL + "_NOT_EXIST";
    private static final String WRONG_PASSWORD = GIVEN_USER_PASSWORD + "_WRONG";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTYxNDY2ODE2NiwiZXhwIjox" +
            "NjE0NjY4MTY2fQ.0DARRfpDRIQLjkJyX5K4UvJNO-0x9fAw3-6YPCA3tdA";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("Post /session 는")
    class Describe_session {

        @Nested
        @DisplayName("이메일과 암호가 주어지면")
        class Context_with_email_and_password {
            final LoginRequest requestDto =
                    new LoginRequest(GIVEN_USER_EMAIL, GIVEN_USER_PASSWORD);

            @BeforeEach
            void setUp() {
                given(authenticationService.authenticate(any(LoginRequest.class)))
                        .willReturn(VALID_TOKEN);
            }

            @DisplayName("201 Created 상태를 응답한다")
            @Test
            void It_responds_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isCreated());
            }
        }

        @Nested
        @DisplayName("등록되지 않는 이메일이 주어지면")
        class Context_with_not_exist_email {
            final LoginRequest request = new LoginRequest(NOT_EXIST_EMAIL, GIVEN_USER_PASSWORD);

            @BeforeEach
            void setUp() {
                given(authenticationService.authenticate(any(LoginRequest.class)))
                        .willThrow(new IllegalArgumentException(WRONG_DATA));
            }

            @DisplayName("400 BadRequest 상태를 응답한다")
            @Test
            void It_responds_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 주어지면")
        class Context_with_wrong_password {
            final LoginRequest request = new LoginRequest(GIVEN_USER_EMAIL, WRONG_PASSWORD);

            @BeforeEach
            void setUp() {
                given(authenticationService.authenticate(any(LoginRequest.class)))
                        .willThrow(new IllegalArgumentException(WRONG_DATA));
            }

            @DisplayName("400 BadRequest 상태를 응답한다")
            @Test
            void It_responds_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
