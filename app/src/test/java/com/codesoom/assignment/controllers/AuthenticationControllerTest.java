package com.codesoom.assignment.controllers;


import com.codesoom.assignment.AuthenticationTestFixture;
import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserAuthenticationFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("AuthenticationController 클래스")
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationService authenticationService;

    private final User GENERAL_USER = AuthenticationTestFixture.GENERAL_USER;
    private final User NON_EXISTENT_USER = AuthenticationTestFixture.NON_EXISTENT_USER;


    @BeforeEach
    void setUp() {
        Mockito.reset(authenticationService);
        final UserLoginData VALID_LOGIN_DATA =
                new UserLoginData(GENERAL_USER.getEmail(), GENERAL_USER.getPassword());
        final UserLoginData NON_EXISTENT_LOGIN_DATA =
                new UserLoginData(NON_EXISTENT_USER.getEmail(), GENERAL_USER.getPassword());
        final UserLoginData UNAUTHORIZED_LOGIN_DATA =
                new UserLoginData(GENERAL_USER.getEmail(), NON_EXISTENT_USER.getPassword());


        given(authenticationService.createSession(VALID_LOGIN_DATA))
                .willReturn(AuthenticationTestFixture.VALID_TOKEN);
        given(authenticationService.createSession(NON_EXISTENT_LOGIN_DATA))
                .willThrow(new UserAuthenticationFailedException(NON_EXISTENT_LOGIN_DATA));
        given(authenticationService.createSession(UNAUTHORIZED_LOGIN_DATA))
                .willThrow(new UserAuthenticationFailedException(UNAUTHORIZED_LOGIN_DATA));
    }

    @Nested
    @DisplayName("POST /session 요청은")
    class Describe_POST_session {

        ResultActions subject(UserLoginData loginData) throws Exception {
            return mockMvc.perform(
                    post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginData))
            );
        }

        @Nested
        @DisplayName("유효한 email과 password가 주어진다면")
        class Context_with_email_password {
            String givenEmail = GENERAL_USER.getEmail();
            String givenPassword = GENERAL_USER.getPassword();

            @DisplayName("201코드와 token을 응답한다")
            @Test
            void it_returns_201_with_token() throws Exception {
                UserLoginData loginData = UserLoginData.builder()
                        .email(givenEmail)
                        .password(givenPassword)
                        .build();

                subject(loginData)
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.accessToken").value(AuthenticationTestFixture.VALID_TOKEN));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 email이 주어진다면")
        class Context_with_not_exist_email {
            String givenEmail = NON_EXISTENT_USER.getEmail();
            String givenPassword = GENERAL_USER.getPassword();

            @DisplayName("400 코드를 응답한다")
            @Test
            void it_returns_400_code() throws Exception {
                UserLoginData loginData = UserLoginData.builder()
                        .email(givenEmail)
                        .password(givenPassword)
                        .build();

                subject(loginData)
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 password가 주어진다면")
        class Context_with_invalid_password {
            String givenEmail = GENERAL_USER.getEmail();
            String givenPassword = NON_EXISTENT_USER.getPassword();

            @DisplayName("400 코드를 응답한다")
            @Test
            void it_returns_400_code() throws Exception {
                UserLoginData loginData = UserLoginData.builder()
                        .email(givenEmail)
                        .password(givenPassword)
                        .build();

                subject(loginData)
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
