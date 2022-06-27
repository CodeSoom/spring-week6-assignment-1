package com.codesoom.assignment.controllers;

import com.codesoom.assignment.Utf8WebTest;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Utf8WebTest
@DisplayName("SessionController 클래스")
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$";
    private final String EXIST_EMAIL = "codesoom1@gmail.com";
    private final String NOT_EXIST_EMAIL = "codesoom2@gmail.com";
    private final String VALID_PASSWORD = "1234!@#$";
    private final String INVALID_PASSWORD = "!@#$1234";

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .id(1L)
                .email(EXIST_EMAIL)
                .password(VALID_PASSWORD)
                .build());
    }

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("[POST] /session")
    class Describe_post {

        private UserLoginData userLoginData;

        @Nested
        @DisplayName("등록된 user 의 유효한 로그인 요청이 주어진다면")
        class Context_with_valid_userLoginData {

            @BeforeEach
            void setUp() {
                userLoginData = userLoginData.builder()
                        .email(EXIST_EMAIL)
                        .password(VALID_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("201 을 응답하고 액세스 토큰을 응답한다.")
            void It_responses_201_and_returns_accessToken() throws Exception {
                mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(userLoginData))
                )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.accessToken", matchesPattern(JWT_REGEX)));
            }
        }

        @Nested
        @DisplayName("등록된 user 의 email 과 일치하지 않으면")
        class Context_with_userLoginData_with_not_existed_email {

            private UserLoginData UserLoginDataWithNotExistedEmail;

            @BeforeEach
            void setUp() {
                UserLoginDataWithNotExistedEmail = userLoginData.builder()
                        .email(NOT_EXIST_EMAIL)
                        .password(VALID_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("404 를 응답한다.")
            void It_responses_400() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toJson(UserLoginDataWithNotExistedEmail))
                        )
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("등록된 user 의 패스워드와 일치하지 않으면")
        class Context_with_userLoginData_with_invalid_password {

            private UserLoginData userLoginDataWithInvalidPassword;

            @BeforeEach
            void setUp() {
                userLoginDataWithInvalidPassword = userLoginData.builder()
                        .email(EXIST_EMAIL)
                        .password(INVALID_PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("400 를 응답한다.")
            void It_responses_400() throws Exception {
                mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(userLoginDataWithInvalidPassword))
                        )
                        .andExpect(status().isBadRequest());
            }
        }

        private String toJson(UserLoginData userLoginData) throws JsonProcessingException {
            return new ObjectMapper().writeValueAsString(userLoginData);
        }
    }
}
