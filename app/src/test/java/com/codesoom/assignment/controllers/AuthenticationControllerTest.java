package com.codesoom.assignment.controllers;

import com.codesoom.assignment.utils.JwtUtils;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthenticationController 클래스의")
public class AuthenticationControllerTest {
    public static final String SESSION_PATH = "/session";
    public static final String USER_PATH = "/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils utils;

    private final Map<String, Object> registerData = Map.of(
            "email", "qjawlsqjacks@naver.com",
            "name", "박범진",
            "password", "1234"
    );
    private final Map<String, Object> loginData = Map.of(
            "email", "qjawlsqjacks@naver.com",
            "password", "1234"
    );

    private ResultActions postRequest(String path, Map<String, Object> requestBody) throws Exception {
        return mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("인증된 유저라면")
        class Context_with_authentication {
            private void registerUser() throws Exception {
                postRequest(USER_PATH, registerData);
            }

            private final String token = utils.encode((String) registerData.get("email"));

            @BeforeEach
            void prepare() throws Exception {
                registerUser();
            }

            @Test
            @DisplayName("토큰과 상태코드 201을 응답한다")
            void It_respond_token() throws Exception {
                postRequest(SESSION_PATH, loginData)
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$").value(token));
            }
        }

        @Nested
        @DisplayName("인증되지 않은 유저라면")
        class Context_without_authentication {
            @Test
            @DisplayName("예외 메시지와 상태코드 400을 응답한다")
            void It_respond_exception() throws Exception {
                postRequest(SESSION_PATH, loginData)
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").isString());
            }
        }
    }
}
