package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;
    private AuthenticationService authenticationService;
    private ObjectMapper mapper = new ObjectMapper();

    private final String SESSION_PATH = "/session";

    @BeforeEach
    void setUp() {
        authenticationService = mock(AuthenticationService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new SessionController(authenticationService))
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .build();
    }

    @Nested
    @DisplayName("handleMethodArgumentNotValid()")
    class Describe_MethodArgumentNotValid{

        @Nested
        @DisplayName("검증에 실패한다면")
        class Context_{

            private final UserLoginData loginData = new UserLoginData("1" , "123");

            @Test
            @DisplayName("에러 응답 DTO를 반환한다.")
            void It_() throws Exception {
                final String wrongContent = mapper.writeValueAsString(loginData);
                mockMvc.perform(post(SESSION_PATH)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(wrongContent))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string(containsString("email")))
                        .andExpect(content().string(containsString("password")));
            }
        }
    }
}
