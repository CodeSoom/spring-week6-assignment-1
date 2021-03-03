package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginDto;
import com.codesoom.assignment.errors.InvalidUserException;
import com.codesoom.assignment.utils.JwtUtil;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    JwtUtil jwtUtil;

    private User validUser;
    private UserLoginDto userLoginDto;

    private final String EMAIL = "rhfpdk92@naver.com";
    private final String PASSWORD = "1234";

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .id(1L)
                .name("양승인")
                .password(PASSWORD)
                .email(EMAIL)
                .build();

        userLoginDto = UserLoginDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Nested
    @DisplayName("POST - /session 요청은")
    class Describe_post_session {

        @Nested
        @DisplayName("적합한 유저 정보가 주어지면")
        class Context_exist_login_data_and_user {

            @BeforeEach
            void setUp() {
                given(userService.validUser(userLoginDto)).willReturn(validUser);
                given(authenticationService.login(any())).willReturn(ACCESS_TOKEN);
            }

            @Test
            @DisplayName("응답코드는 201이며 토큰을 응답한다.")
            void it_return_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").value(ACCESS_TOKEN));
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
    }
}
