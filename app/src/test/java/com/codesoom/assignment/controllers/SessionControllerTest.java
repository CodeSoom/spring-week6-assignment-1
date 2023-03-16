package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("mugeon@email.com")
                .password("1234")
                .build();
        given(authenticationService.login(userLoginRequest)).willReturn("a","b","c");
    }
    @Test
    @DisplayName("Create")
    public void longin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/session"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString(".")));
    }
}