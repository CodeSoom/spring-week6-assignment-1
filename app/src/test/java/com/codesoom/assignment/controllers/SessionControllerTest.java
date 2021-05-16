package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginDetail;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("SessionController 클래스")
@WebMvcTest(SessionController.class)
class SessionControllerTest {

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdakD";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private UserLoginDetail validUserLoginDetail;
    private UserLoginDetail invalidUserLoginDetail;

    @BeforeEach
    void setUp() {
        validUserLoginDetail = UserLoginDetail.builder()
                .email("min@gmail.com")
                .password("1q2w3e!")
                .build();

        invalidUserLoginDetail = UserLoginDetail.builder()
                .email("invalid@gmail.com")
                .password("invalid")
                .build();
    }

    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_post_session_request {
        @Nested
        @DisplayName("유효한 정보가 주어지면")
        class Context_with_valid_detail {
            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(UserLoginDetail.class)))
                        .willReturn(VALID_TOKEN);
            }

            @DisplayName("201 코드와 토큰을 응답한다")
            @Test
            void it_responses_201_and_valid_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserLoginDetail)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").exists())
                        .andExpect(content().string(containsString(VALID_TOKEN)));
            }
        }
    }
}
