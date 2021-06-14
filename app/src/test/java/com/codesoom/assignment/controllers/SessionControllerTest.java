package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.PasswordNotCorrectException;
import com.codesoom.assignment.errors.UserNotFoundException;
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
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    private static final String EXISTED_EMAIL = "example@login.com";
    private static final String NOT_EXISTED_EMAIL = "not-existed@login.com";
    private static final String CORRECT_PASSWORD = "password";
    private static final String INCORRECT_PASSWORD = "incorrect";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyRW1haWwiOiJleGFtcGxlQ" +
                                              "GxvZ2luLmNvbSJ9.VC6A_h3VehY_zvmxNGtgdwCc6zeFRBcdKSkj2rNB93Y";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private UserLoginData userLoginData;

    @Nested
    @DisplayName("POST /session 은")
    class Describe_login {

        @Nested
        @DisplayName("주어진 사용자 정보를 사용해 로그인에 성공하면")
        class Context_with_valid_userLoginData {

            @BeforeEach
            void prepareUserLoginData() {
                userLoginData = UserLoginData.builder()
                                             .email(EXISTED_EMAIL)
                                             .password(CORRECT_PASSWORD)
                                             .build();

                given(authenticationService.login(any(UserLoginData.class))).willReturn(VALID_TOKEN);
            }

            @Test
            @DisplayName("HttpStatus 201 Created 를 응답한다")
            void it_returns_httpStatus_created() throws Exception {
                String content = objectMapper.writeValueAsString(userLoginData);
                mockMvc.perform(post("/session")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isCreated())
                       .andExpect(content().string(containsString(VALID_TOKEN)));

                verify(authenticationService, atLeastOnce()).login(any(UserLoginData.class));
            }
        }

        @Nested
        @DisplayName("이메일이 존재하지 않아 로그인에 실패하면")
        class Context_with_not_existed_email_in_userLoginData {

            @BeforeEach
            void prepareNotExistedEmail() {
                userLoginData = UserLoginData.builder()
                                             .email(NOT_EXISTED_EMAIL)
                                             .password(CORRECT_PASSWORD)
                                             .build();

                given(authenticationService.login(any(UserLoginData.class))).willThrow(new UserNotFoundException(NOT_EXISTED_EMAIL));
            }

            @Test
            @DisplayName("HttpStatus 404 Not Found 를 응답한다")
            void it_returns_httpStatus_not_found() throws Exception {
                String content = objectMapper.writeValueAsString(userLoginData);
                mockMvc.perform(post("/session")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isNotFound());

                verify(authenticationService, atLeastOnce()).login(any(UserLoginData.class));
            }
        }

        @Nested
        @DisplayName("패스워드가 틀려 로그인에 실패하면")
        class Context_with_incorrect_password_in_userLoginData {

            @BeforeEach
            void prepareIncorrectPassword() {
                userLoginData = UserLoginData.builder()
                                             .email(EXISTED_EMAIL)
                                             .password(INCORRECT_PASSWORD)
                                             .build();

                given(authenticationService.login(any(UserLoginData.class))).willThrow(new PasswordNotCorrectException());
            }

            @Test
            @DisplayName("HttpStatus 400 BadRequest 를 응답한다")
            void it_returns_httpStatus_unauthorized() throws Exception {
                String content = objectMapper.writeValueAsString(userLoginData);
                mockMvc.perform(post("/session")
                               .contentType(MediaType.APPLICATION_JSON)
                               .content(content))
                       .andExpect(status().isBadRequest());

                verify(authenticationService, atLeastOnce()).login(any(UserLoginData.class));
            }
        }
    }
}
