package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.EmailNotFoundException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("로그인 메소드는")
    class Describe_login{

        @Nested
        @DisplayName("유효한 사용자 정보로 요청했을 때")
        class Context_with_valid_user{
            SessionRequestData reqData = SessionRequestData.builder()
                    .email("email")
                    .password("password")
                    .build();

            @BeforeEach
            void setUp() {
                given(authenticationService.login(reqData))
                        .willReturn(any(String.class));
            }

            @Test
            void it_responses_status_created() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(reqData))
                        )
                        .andExpect(status().isCreated());
            }
        }

        @Nested
        @DisplayName("올바르지 않은 사용자 정보로 요청했을 때")
        class Context_with_invalid_user{
            String invalidEmail = "invalidEmail";
            SessionRequestData invalidReq = SessionRequestData.builder()
                    .email(invalidEmail)
                    .password("password")
                    .build();

            @BeforeEach
            void setUp() {
                given(authenticationService.login(invalidReq))
                        .willThrow(new EmailNotFoundException(invalidEmail));
            }

            // Test 통과하지 못함. Expected :404 Actual :201
            @Test
            void It_responses_status_notFound() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(invalidReq))
                        )
                        .andExpect(status().isNotFound());
            }
        }
    }

}
