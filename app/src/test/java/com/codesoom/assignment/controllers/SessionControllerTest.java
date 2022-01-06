package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SessionController 클래스")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    LoginData testLoginData;

    @BeforeEach
    void setUp() {
        testLoginData = LoginData.builder()
                .email("pjh0819@codesom.com")
                .password("123456")
                .build();
    }

    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_post {

        @Nested
        @DisplayName("등록된 유저의 LoginData가 주어진다면")
        class Context_with_registered_loginData {

            @BeforeEach
            void prepare() {
                User user = User.builder()
                        .email(testLoginData.getEmail())
                        .password(testLoginData.getPassword())
                        .build();

                userRepository.save(user);
            }

            @Test
            @DisplayName("access token을 응답합니다.")
            void it_return_accessToken() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginDataToContent(testLoginData)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.accessToken").value(containsString(".")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("등록되지 않은 유저의 LoginData가 주어진다면")
        class Context_with_unregistered_loginData {

            @Test
            @DisplayName("404(Not Found)를 응답합니다.")
            void it_return_accessToken() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginDataToContent(testLoginData)))
                        .andExpect(status().isNotFound())
                        .andDo(print());
            }
        }
    }

    private String loginDataToContent(LoginData loginData) throws JsonProcessingException {
        return objectMapper.writeValueAsString(loginData);
    }
}
