package com.codesoom.assignment.auth.controller;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.dto.AuthenticationRequestDto;
import com.codesoom.assignment.auth.dto.SessionResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SessionController 클래스")
class SessionMockMvcControllerTest {
    private static final String USER_EMAIL = "test@test.com";
    private static final String USER_PASSWORD = "password";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTYxNDY2ODE2NiwiZXhwIjox" +
            "NjE0NjY4MTY2fQ.0DARRfpDRIQLjkJyX5K4UvJNO-0x9fAw3-6YPCA3tdA";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /session은")
    class Describe_session {
        AuthenticationRequestDto authenticationRequestDto;

        @Nested
        @DisplayName("등록할 사용자가 주어지면")
        class Context_with_user {
            @BeforeEach
            void setUp() {
                authenticationRequestDto = new AuthenticationRequestDto(USER_EMAIL, USER_PASSWORD);
                given(authenticationService.authenticate(authenticationRequestDto))
                        .willReturn(new SessionResponseData(VALID_TOKEN));
            }

            @DisplayName("201 Created 상태를 응답한다.")
            @Test
            void It_responds_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                        .andExpect(status().isCreated());
            }
        }
    }
}
