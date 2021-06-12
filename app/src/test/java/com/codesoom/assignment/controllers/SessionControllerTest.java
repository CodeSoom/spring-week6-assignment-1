package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
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
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SessionController")
@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;
    private ObjectMapper objectMapper = new ObjectMapper();

    private String sesseionUrl = "/session";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @Nested
    @DisplayName("login 메서드는")
    class Describe_Login {
        @Nested
        @DisplayName("올바른 사용자 정보를 요청을 하면")
        class Context_Valid_Login {
            @BeforeEach
            void setUpValidLogin(){
                given(authenticationService.login(any(SessionRequestData.class)))
                        .willReturn(VALID_TOKEN);
            }

            @Test
            @DisplayName("Http상태코드 CREATED(201)을 전달한다.")
            void valid_login_status() throws Exception {
                mockMvc.perform(post(sesseionUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
            }

            @Test
            @DisplayName("토큰을 발급한다.")
            void valid_login_return_token() throws Exception {
                mockMvc.perform(post(sesseionUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(containsString(VALID_TOKEN)));
            }
        }
    }
}
