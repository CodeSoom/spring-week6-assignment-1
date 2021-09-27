package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    private static final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$";

    private static final String EMAIL = "sprnd645@gmail.com";
    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
            .id(1L)
            .email(EMAIL)
            .password(PASSWORD)
            .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_post_session {

        private LoginRequestDto loginRequestDto;

        @Nested
        @DisplayName("올바른 로그인 요청이 주어질 때")
        class Context_validLoginRequest {

            @BeforeEach
            void setUp() {
                loginRequestDto = LoginRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();
            }

            @Test
            @DisplayName("201을 응답하고 액세스 토큰을 리턴한다")
            void it_response_201_and_returns_accessToken() throws Exception {
                mockMvc.perform(
                    post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginRequestDto))
                )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.accessToken", matchesPattern(JWT_REGEX)));
            }

        }

        @Nested
        @DisplayName("삭제된 회원일 경우")
        class Context_deletedUser {

            @BeforeEach
            void setUp() {
                loginRequestDto = LoginRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();

                userRepository.deleteAll();
            }

            @Test
            @DisplayName("404를 응답한다")
            void it_response_404() throws Exception {
                mockMvc.perform(
                    post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginRequestDto))
                )
                    .andExpect(status().isNotFound());
            }
        }


        @Nested
        @DisplayName("회원의 비밀번호가 일치하지 않을 경우")
        class Context_passwordNotMatch {

            @BeforeEach
            void setUp() {
                loginRequestDto = LoginRequestDto.builder()
                    .email(EMAIL)
                    .password("NOT_MATCH_PASSWORD")
                    .build();
            }

            @Test
            @DisplayName("400를 응답한다")
            void it_response_400() throws Exception {
                mockMvc.perform(
                    post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginRequestDto))
                )
                    .andExpect(status().isBadRequest());
            }
        }
    }

    private String toJson(LoginRequestDto user) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(user);
    }
}
