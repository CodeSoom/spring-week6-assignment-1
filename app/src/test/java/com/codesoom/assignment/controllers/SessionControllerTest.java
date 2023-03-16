package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginRequest;
import com.codesoom.assignment.errors.LoginNotMatchException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.google.common.base.CharMatcher.any;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJ1c2VySWQiOjF9.bOQfc4mYhcmbCH6JjPuzSc6eRJEr_A0d8tVrPtxE9aU";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJ1c2VySWQiOjF9.bOQfc4mYhcmbCH6JjPuzSc6eRJEr_A0d8tVrPtxE9aU" + "ERROR";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private UserLoginRequest request;

    @BeforeEach
    void setUp() {
        request = UserLoginRequest.builder()
                .email("mugeon@email.com")
                .password("1234")
                .build();
//        given(authenticationService.login(ArgumentMatchers.any(UserLoginRequest.class))).willReturn(VALID_TOKEN);
    }

    @Test
    @DisplayName("Create")
    public void longin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString(VALID_TOKEN)));
    }

    @DisplayName("아이디||비밀번호 오류")
    @Test
    void loginWithInvalidParameter() throws Exception {
        given(authenticationService.login(ArgumentMatchers.any(UserLoginRequest.class))).willThrow(LoginNotMatchException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/session")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"));
    }
}
