package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionLoginData;
import com.codesoom.assignment.errors.LoginWrongPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("SessionController 클래스")
class SessionControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private ObjectMapper objectMapper = new ObjectMapper();


    String contentSessionLoginData;
    SessionLoginData sessionLoginData;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        sessionLoginData = SessionLoginData.builder()
                .email("tester@example.com")
                .password("test")
                .build();
        contentSessionLoginData = objectMapper.writeValueAsString(sessionLoginData);
        System.out.println("sessionLoginData = " + sessionLoginData);

    }


    @Nested
    @DisplayName("POST /session은")
    class Describe_request_post_to_session_path {

        @Nested
        @DisplayName("존재하는 id와 올바른 pw가 주어지면")
        class Context_with_a_exist_id_and_right_pw{

            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(SessionLoginData.class))).willReturn(VALID_TOKEN);
            }

            @Test
            @DisplayName("액세스 토큰을 응답합니다.")
            void it_responses_token() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(contentSessionLoginData))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(VALID_TOKEN)));
            }
        }

        @Nested
        @DisplayName("존재하는 id와 잘못된 pw가 주어지면")
        class Context_with_a_exist_id_and_wrong_pw{

            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(SessionLoginData.class)))
                        .willThrow(new LoginWrongPasswordException());
            }

            @Test
            @DisplayName("400(BadRequest)을 응답합니다.")
            void it_responses_token() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(contentSessionLoginData))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_a_not_exist_id{

            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(SessionLoginData.class)))
                        .willThrow(new UserNotFoundException(sessionLoginData.getEmail()));
            }

            @Test
            @DisplayName("404(NotFound)을 응답합니다.")
            void it_responses_token() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(contentSessionLoginData))
                        .andExpect(status().isNotFound());
            }
        }

    }
}
