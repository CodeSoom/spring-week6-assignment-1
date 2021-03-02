package com.codesoom.assignment.auth.controller;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.dto.AuthenticationRequestDto;
import com.codesoom.assignment.user.application.UserEmailNotFoundException;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SessionController 클래스")
@WebMvcTest(SessionController.class)
class AuthenticationControllerTest {
    private static final String GIVEN_USER_EMAIL = "test@test.com";
    private static final String GIVEN_PASSWORD = "password";
    private static final String NOT_EXIST_EMAIL = "not_exist@test.com";
    private static final String WRONG_PASSWORD = "wrong_password";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTYxNDY2ODE2NiwiZXhwIjox" +
            "NjE0NjY4MTY2fQ.0DARRfpDRIQLjkJyX5K4UvJNO-0x9fAw3-6YPCA3tdA";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        Mockito.reset(authenticationService);
    }

    @Nested
    @DisplayName("Post /session 는")
    class Describe_session {
        AuthenticationRequestDto requestDto;

        @Nested
        @DisplayName("이메일과 암호가 주어지면")
        class Context_with_email_and_password {

            @BeforeEach
            void setUp() {
                requestDto = new AuthenticationRequestDto(GIVEN_USER_EMAIL, GIVEN_PASSWORD);
                given(authenticationService.authenticate(GIVEN_USER_EMAIL, GIVEN_PASSWORD))
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

            @BeforeEach
            void setUp() {
                requestDto = new AuthenticationRequestDto(NOT_EXIST_EMAIL, GIVEN_PASSWORD);
                given(authenticationService.authenticate(NOT_EXIST_EMAIL, GIVEN_PASSWORD))
                        .willThrow(new UserEmailNotFoundException(NOT_EXIST_EMAIL));
            }

            @DisplayName("400 BadRequest 상태를 응답한다")
            @Test
            void It_responds_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 주어지면")
        class Context_with_wrong_password {

            @BeforeEach
            void setUp() {
                requestDto = new AuthenticationRequestDto(GIVEN_USER_EMAIL, WRONG_PASSWORD);
                given(authenticationService.authenticate(GIVEN_USER_EMAIL, WRONG_PASSWORD))
                        .willThrow(new IllegalArgumentException("잘못된 비밀번호 입니다."));
            }

            @DisplayName("400 BadRequest 상태를 응답한다")
            @Test
            void It_responds_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
