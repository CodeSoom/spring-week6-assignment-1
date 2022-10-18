package com.codesoom.assignment.controllers;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.request.LoginRequest;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("SessionController 클래스")
class SessionControllerTest {

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "t!@#$%";
    private static final String WRONG_PASSWORD = "wrong_t!@#$%";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("존재하는 회원 정보로 요청이 오면")
        class Context_with_valid_request {
            private final LoginRequest request = new LoginRequest(EMAIL, PASSWORD);
            private Long userId;

            @BeforeEach
            void setUp() {
                User user = userRepository.save(User.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build());

                userId = user.getId();
            }

            @Test
            @DisplayName("토큰과 함께 201 응답을 생성한다")
            void it_responds_created() throws Exception {
                mockMvc.perform(post("/session")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.token").value(jwtUtil.createToken(userId)))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 이메일로 요청이 오면")
        class Context_with_wrong_email {
            private final LoginRequest request = new LoginRequest(EMAIL, PASSWORD);

            @BeforeEach
            void setUp() {
                User user = userRepository.save(User.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build());

                userRepository.delete(user);
            }

            @Test
            @DisplayName("404 응답을 생성한다")
            void it_responds_not_found() throws Exception {
                mockMvc.perform(post("/session")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("틀린 비밀번호로 요청이 오면")
        class Context_with_wrong_password {
            private final LoginRequest request = new LoginRequest(EMAIL, WRONG_PASSWORD);

            @BeforeEach
            void setUp() {
                userRepository.save(User.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build());
            }

            @Test
            @DisplayName("400 응답을 생성한다")
            void it_responds_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
