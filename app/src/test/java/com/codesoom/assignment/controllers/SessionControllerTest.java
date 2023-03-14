package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.PasswordMismatchException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("유효한 Email과 Password를 입력하면")
    class ValidEmailAndPassword{

        @BeforeEach
        void setUp(){
            given(authenticationService.login(any(UserLoginData.class))).willReturn(VALID_TOKEN);
        }
        @Test
        void successLogin() throws Exception {
            mockMvc.perform(post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"test@naver.com\",\"password\":\"1234\"}"))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(VALID_TOKEN)));
        }

    }

    @Nested
    @DisplayName("유효하지 않은 Email과 Password를 입력하면")
    class InvalidEmailAndPassword{

        @BeforeEach
        void setUp(){
            given(authenticationService.login(any(UserLoginData.class)))
                    .willThrow(new PasswordMismatchException());
        }
        @Test
        void failLogin() throws Exception {
            mockMvc.perform(post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"test@naver.com\",\"password\":\"1234\"}"))
                    .andExpect(status().isBadRequest());
        }

    }

}