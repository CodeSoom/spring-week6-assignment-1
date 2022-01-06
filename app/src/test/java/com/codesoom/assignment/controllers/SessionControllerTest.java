package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("SessionController")
class SessionControllerTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "000";

    private static final String EXIST_EMAIL = "exist@test.com";
    private static final String NOT_EXIST_EMAIL = "notexist@test.com";

    private static final String PASSWORD = "1234";
    private static final String WRONG_PASSWORD = "wrong";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Nested
    @DisplayName("post /session 요청시")
    class Describe_post_path_session {
        @BeforeEach
        void setUp() {
            given(authenticationService.login(any(UserLoginData.class))).willReturn(VALID_TOKEN);
        }

        @Nested
        @DisplayName("올바른 사용자 정보가 주어지면")
        class Context_With_correct_user {
            private UserLoginData userLoginData;
            private String userContext;

            @BeforeEach
            void setUp() throws JsonProcessingException {
                userLoginData = UserLoginData.builder()
                        .email(EXIST_EMAIL)
                        .password(PASSWORD)
                        .build();

                userContext = objectMapper.writeValueAsString(userLoginData);
            }

            @Test
            @DisplayName("요청이 성공적으로 처리되었음을 응답한다.")
            void it_status_created() throws Exception {
                mockMvc.perform(post("/session")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userContext))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(VALID_TOKEN)));

            }
        }
    }
    //TODO 유효하지 않은 유저 정보 테스트
}
