package com.codesoom.assignment.controllers;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("AuthenticationController 클래스")
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {
  final String VALID_EMAIL = "rlawlstjd@gmail.com";
  final String VALID_PASSWORD = "12345";
  final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5VoLz2Ug0E6jQK_anP6RND1YHpzlBdxsR2ORgYef_aQ";

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  AuthenticationService authenticationService;

  @BeforeEach
  void setUp() {
    Mockito.reset(authenticationService);

    given(authenticationService.createSession(VALID_EMAIL, VALID_PASSWORD)).willReturn(VALID_TOKEN);
  }

  @Nested
  @DisplayName("POST /session 요청은")
  class Describe_POST_session {

    @Nested
    @DisplayName("유효한 email과 password가 주어진다면")
    class Context_with_email_password {

      String givenEmail = VALID_EMAIL;
      String givenPassword = VALID_PASSWORD;

      @DisplayName("token을 응답한다")
      @Test
      void it_returns_token() throws Exception {
        UserLoginData loginData = UserLoginData.builder()
            .email(givenEmail)
            .password(givenPassword)
            .build();

        mockMvc.perform(
            post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginData))
        )
            .andExpect(status().isCreated())
            .andExpect(content().string(VALID_TOKEN));
      }
    }
  }
}