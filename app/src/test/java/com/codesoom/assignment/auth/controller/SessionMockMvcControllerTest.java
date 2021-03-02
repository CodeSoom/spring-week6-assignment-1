package com.codesoom.assignment.auth.controller;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@AutoConfigureMockMvc
@DisplayName("SessionController 클래스")
class SessionMockMvcControllerTest {
    private static final String USER_EMAIL = "test@test.com";
    private static final String USER_PASSWORD = "password";

    private static final String NOT_EXIST_EMAIL = "not_exist@test.com";
    private static final String WRONG_PASSWORD = "not_password";


    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private AuthenticationRequestDto authenticationRequestDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(wac).addFilter(((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        })).build();
    }

    @Nested
    @DisplayName("등록할 사용자가 주어지면")
    class Context_with_user {
        @BeforeEach
        void setUp() {
            authenticationRequestDto = AuthenticationRequestDto.builder()
                    .email(USER_EMAIL)
                    .password(USER_PASSWORD)
                    .build();
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

    @Nested
    @DisplayName("생성 정보가 없으면")
    class Context_not_existed_email {
        @BeforeEach
        void setUp() {
            authenticationRequestDto = AuthenticationRequestDto.builder()
                    .email(NOT_EXIST_EMAIL)
                    .password(USER_PASSWORD)
                    .build();
        }

        @DisplayName("400 Bad Request 상태를 응답한다.")
        @Test
        void It_responds_bad_request() throws Exception {
            mockMvc.perform(post("/session")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("잘못된 비밀번호가 입력되면")
    class Context_with_wrong_password {
        @BeforeEach
        void setUp() {
            authenticationRequestDto = AuthenticationRequestDto.builder()
                    .email(USER_EMAIL)
                    .password(WRONG_PASSWORD)
                    .build();
        }

        @DisplayName("400 Bad Request 상태를 응답한다.")
        @Test
        void It_responds_bad_request() throws Exception {
            mockMvc.perform(post("/sessions")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                    .andExpect(status().isBadRequest());
        }
    }
}
}
