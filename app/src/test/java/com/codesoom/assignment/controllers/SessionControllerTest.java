package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private final String EMAIL = "codesoom@gamil.com";
    private final String PASSWORD = "1234";

    private UserRegistrationData invalidLoginData;
    private UserRegistrationData validLoginData;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

    }

    @Nested
    @DisplayName("Session 요청은")
    class Describe_session {
        @Nested
        @DisplayName("올바른 로그인 요칭이 주어질 떄")
        class Context_validLogin {

            @BeforeEach
            void setUp() {
                given(authenticationService.login()).willReturn(VALID_TOKEN);
            }

            @Test
            @DisplayName("201을 응답한다.")
            void it_return_status() throws Exception {
                mockMvc.perform(post("/session"))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(VALID_TOKEN)));
            }
        }
//
//        @Nested
//        @DisplayName("비밀번호가 잘못된 경우")
//        class Context_wrong_password {
//
//            private final String WRONG_PASSWORD = "31414";
//
//            @Test
//            @DisplayName("400을 응답한다.")
//            void it_return_status() throws Exception {
//                UserRegistrationData invalidLoginData = UserRegistrationData.builder()
//                        .email(EMAIL)
//                        .password(WRONG_PASSWORD)
//                        .build();
//
//                mockMvc.perform(post("/session")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(String.valueOf(invalidLoginData)))
//                        .andExpect(status().isBadRequest());
//            }
//        }
//
//        @Nested
//        @DisplayName("user가 없을 경우")
//        class Context_without_user {
//
//            @BeforeEach
//            void setUp(){
//                userRepository.deleteAll();
//            }
//
//            @Test
//            @DisplayName("404를 응답한다.")
//            void it_return_status() throws Exception {
//                mockMvc.perform(post("/session")
//                                .contentType(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isBadRequest());
//            }
//        }
    }
}
