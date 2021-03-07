package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginDto;
import com.codesoom.assignment.errors.InvalidUserException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    private static final String ACCESS_TOKEN = "sample.access.token";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private UserLoginDto userLoginDto;
    private UserLoginDto invalidLoginDto;

    private final String EMAIL = "rhfpdk92@naver.com";
    private final String PASSWORD = "1234";

    @BeforeEach
    void setUp() {

        userLoginDto = UserLoginDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        invalidLoginDto = UserLoginDto.builder()
                .email(EMAIL)
                .password("wrong")
                .build();
    }

    @Nested
    @DisplayName("POST - /session 요청은")
    class Describe_post_session {

        @Nested
        @DisplayName("적합한 유저 정보가 주어지면")
        class Context_with_valid_user {

            @BeforeEach
            void setUp() {
                given(authenticationService.login(any(UserLoginDto.class)))
                        .willReturn(ACCESS_TOKEN);
            }

            @Test
            @DisplayName("응답코드는 201이며 토큰을 응답한다.")
            void it_return_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").exists())
                        .andExpect(content().string(containsString(".")));
            }
        }

        @Nested
        @DisplayName("유저 정보가 주어지지 않으면")
        class Context_does_not_exist_login_data {

            @Test
            @DisplayName("bad request와 에러메세지를 응답한다.")
            void it_return_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("message").value("Email or password cannot be blank"));

            }
        }

        @Nested
        @DisplayName("유효하지 않은 유저의 정보가 주어지면")
        class Context_with_invalid_user {
            @BeforeEach
            void setup() {
                given(authenticationService.login(any(UserLoginDto.class)))
                        .willThrow(new InvalidUserException());
            }

            @Test
            @DisplayName("bad request를 응답한다.")
            void it_return_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidLoginDto)))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("message").exists());
            }
        }
    }
}
