package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(SessionController.class)
@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Nested
    @DisplayName("login()")
    class Describe_Login{

        @Nested
        @DisplayName("로그인 DTO 검증을 실패한다면")
        class Context_FailedValidationLoginData{

            private String emailBlankContent;
            private String passwordSizeErrorContent;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                emailBlankContent = mapper.writeValueAsString(new UserLoginData("" , "password"));
                passwordSizeErrorContent = mapper.writeValueAsString(new UserLoginData("email" , "123"));
            }

            @Test
            @DisplayName("잘못된 요청이라는 응답코드를 반환한다.")
            void It_() throws Exception {
                mockMvc.perform(post("/session")
                                .content(emailBlankContent)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

                mockMvc.perform(post("/session")
                                .content(passwordSizeErrorContent)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("로그인 DTO 검증을 통과한다면")
        class Context_PassedValidationLoginData{

            private UserLoginData loginData;
            private String loginContent;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                loginData = new UserLoginData("email" , "password");
                loginContent = mapper.writeValueAsString(loginData);
            }


            @Nested
            @DisplayName("사용자가 존재하지 않는다면")
            class Context_NotExistedUser{

                @Test
                @DisplayName("자원이 존재하지 않는다는 예외를 던진다.")
                void It_ThrowException() throws Exception {
                    mockMvc.perform(post("/session")
                                    .content(loginContent)
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNotFound());
                }
            }

            @Nested
            @DisplayName("사용자가 존재한다면")
            class Context_ExistedUser{

                private String loginContent;
                private final String name = "user1";
                private final String email = "email1@email.com";
                private final String password = "password1";

                @BeforeEach
                void setUp() throws JsonProcessingException {
                    loginContent = mapper.writeValueAsString(new UserLoginData(email , password));
                }

                @Test
                @DisplayName("JWT를 반환한다.")
                void It_ReturnJWT() throws Exception {
                    final User registerUser = userRepository.save((User.builder()
                                                                        .name(name)
                                                                        .email(email))
                                                                        .password(password)
                                                                        .build());
                    final String token = jwtUtil.encode(registerUser.getId());

                    mockMvc.perform(post("/session")
                                .content(loginContent)
                                .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andExpect(content().string(containsString(token)));
                }
            }
        }
    }
}
