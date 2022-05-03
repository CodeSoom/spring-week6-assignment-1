package com.codesoom.assignment.controllers;

import com.codesoom.assignment.annotations.Utf8MockMvc;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Utf8MockMvc
@DisplayName("/users")
class UserControllerApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final static String VALID_EMAIL = "validUser@google.com";
    private final static String VALID_PASSWORD = "12345678";
    private final static String VALID_NAME = "i_am_valid_name";
    private User validUser;

    @Nested
    @DisplayName("/ (루트) 경로는")
    class Describe_root_path {
        @Nested
        @DisplayName("POST 요청을 받았을 때")
        class Context_post_request {
            private final MockHttpServletRequestBuilder requestBuilder;
            public Context_post_request() {
                requestBuilder = post("/users");
            }

            @Nested
            @DisplayName("유효한 회원가입 정보를 받는다면")
            class Context_valid_user_registration_data {
                private final ResultActions actions;

                public Context_valid_user_registration_data() throws Exception {
                    initUserRepository();

                    actions = mockMvc.perform(requestBuilder
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format(
                                    "{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}"
                                    , VALID_EMAIL, VALID_PASSWORD, VALID_NAME)));
                }

                @Test
                @DisplayName("201 CREATED 응답을 전달한다.")
                void it_responses_201_created() throws Exception {
                    actions.andExpect(status().isCreated());
                }
            }

            @Nested
            @DisplayName("회원가입 정보로 이미 가입된 이메일을 받는다면")
            class Context_already_registered_email {
                private final ResultActions actions;

                public Context_already_registered_email() throws Exception {
                    setUpValidUser();

                    actions = mockMvc.perform(requestBuilder
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.format(
                                    "{\"email\":\"%s\",\"password\":\"%s\",\"name\":\"%s\"}"
                                    , validUser.getEmail(), validUser.getPassword(), validUser.getName())));
                }

                @Test
                @DisplayName("400 BAD REQUEST 응답을 전달한다.")
                void it_responses_400_bad_request() throws Exception {
                    actions.andExpect(status().isBadRequest());
                }
            }
        }
    }

    public void setUpValidUser() {
        initUserRepository();

        validUser = userService.registerUser(UserRegistrationData.builder()
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .name("유효한유저")
                .build());
    }

    public void initUserRepository() {
        userRepository.deleteAll();
    }
}