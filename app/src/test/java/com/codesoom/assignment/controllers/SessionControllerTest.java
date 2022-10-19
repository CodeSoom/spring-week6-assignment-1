package com.codesoom.assignment.controllers;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestDTO;
import com.codesoom.assignment.infra.JpaUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {
    private static final String VALID_TOKEN_BY_USER_ID_1L = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaUserRepository jpaUserRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("알맞은 email 과 password 가 주어지면")
        class Context_with_correct_email_and_password {
            private String requestBody;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                User savedUser = jpaUserRepository.save(
                        User.builder()
                                .id(1L)
                                .email("a@a.com")
                                .password("123456")
                                .build()
                );
                requestBody = objectMapper.writeValueAsString(
                        new LoginRequestDTO(savedUser.getEmail(), savedUser.getPassword())
                );
            }

            @Test
            @DisplayName("token 을 리턴한다")
            void it_returns_token() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(VALID_TOKEN_BY_USER_ID_1L)));
            }
        }

        @Nested
        @DisplayName("알맞지 않은 email 과 password 가 주어지면")
        class Context_with_incorrect_email_and_password {
            private String requestBodyWithIncorrectEmail;
            private String requestBodyWithIncorrectPassword;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                User savedUser = jpaUserRepository.save(
                        User.builder()
                                .id(1L)
                                .email("a@a.com")
                                .password("123456")
                                .build()
                );
                requestBodyWithIncorrectEmail = objectMapper.writeValueAsString(
                        new LoginRequestDTO("b@b.com", savedUser.getPassword())
                );
                requestBodyWithIncorrectPassword = objectMapper.writeValueAsString(
                        new LoginRequestDTO(savedUser.getEmail(), "9999999")
                );

            }

            @Test
            @DisplayName("400 응답을 리턴한다")
            void it_returns_token() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyWithIncorrectEmail))
                        .andExpect(status().isNotFound());

                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyWithIncorrectPassword))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
