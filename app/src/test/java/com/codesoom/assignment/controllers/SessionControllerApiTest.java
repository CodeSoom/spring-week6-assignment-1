package com.codesoom.assignment.controllers;

import com.codesoom.assignment.annotations.Utf8MockMvc;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Utf8MockMvc
@DisplayName("/session")
class SessionControllerApiTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;

    private final static String VALID_EMAIL = "validUser@google.com";
    private final static String VALID_PASSWORD = "12345678";
    private User validUser;


    @Nested
    @DisplayName("/login 경로는")
    class Describe_login_path {
        @Nested
        @DisplayName("POST 요청을 받았을 때")
        class Context_post_request {
            private final MockHttpServletRequestBuilder requestBuilder;

            public Context_post_request() {
                requestBuilder = post("/session/login");
            }

            @Nested
            @DisplayName("유효한 email 과 password 를 받는다면")
            class Context_valid_email_and_password {
                private final ResultActions actions;

                public Context_valid_email_and_password() throws Exception {
                    setUpValidUser();

                    actions = mockMvc.perform(requestBuilder
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format(
                                    "{\"email\":\"%s\",\"password\":\"%s\"}"
                                    , VALID_EMAIL, VALID_PASSWORD)));
                }

                @Test
                @DisplayName("200 OK 응답을 전달한다.")
                void it_responses_200_ok() throws Exception {
                    actions.andExpect(status().isOk());
                }

                @Test
                @DisplayName("JWT 토큰 값을 반환한다.")
                void it_returns_jwt_token() throws Exception {
                    String validToken = jwtUtil.encode(validUser.getId());

                    actions.andExpect(content().string(containsString(validToken)));
                }
            }
        }
    }

    public void setUpValidUser() {
        userRepository.deleteAll();

        validUser = userService.registerUser(UserRegistrationData.builder()
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .name("유효한유저")
                .build());
    }
}