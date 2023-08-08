package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("SessionController 클래스")
class SessionControllerTest {

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private final String AUTH_NAME = "AUTH_NAME";
    private final String AUTH_EMAIL = "auth@foo.com";
    private final String AUTH_PASSWORD = "12345678";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthenticationService authenticationService;
    private UserLoginData AUTH_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(AUTH_PASSWORD)
            .build();

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_유저로그인정보_요청를_받으면 {
            
            @DisplayName("인증토큰을 반환한다.")
            @Test
            void It_returns_token() throws Exception {
                String jsonString = objectMapper.writeValueAsString(AUTH_USER_DATA);

                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(VALID_TOKEN)));
            }
        }
    }
}
