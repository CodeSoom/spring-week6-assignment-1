package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.UserPasswordMismatchException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("SessionController 테스트")
class SessionControllerTest {
    private static final Long ID = 1L;

    private static final String EMAIL = "sample@google.com";
    private static final String UNKNOWN_EMAIL = "UNKNOWN"+ EMAIL;

    private static final String PASSWORD = "sample_password";
    private static final String INVALID_PASSWORD = "INVALID" + PASSWORD;

    private static final String NAME = "sample";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        given(userService.findUserByEmailPassword(eq(EMAIL), eq(PASSWORD)))
                .willReturn(User.builder()
                        .id(ID)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .name(NAME)
                        .build());

        given(userService.findUserByEmailPassword(eq(UNKNOWN_EMAIL), eq(PASSWORD)))
                .willThrow(new UserNotFoundException(UNKNOWN_EMAIL));

        given(userService.findUserByEmailPassword(eq(EMAIL), eq(INVALID_PASSWORD)))
                .willThrow(new UserPasswordMismatchException());

        given(authenticationService.login(ID)).willReturn("aaa.bbb.ccc");
    }

    @Nested
    @DisplayName("login 메서드")
    class DescribeFindUserByEmailPassword {
        @Nested
        @DisplayName("이메일에 해당하는 회원이 있을 경우")
        class ContextWithExistedUserEmail {
            @Test
            @DisplayName("비밀번호가 일치한다면, ACCESS TOKEN과 CREATED 응답코드를 반환한다")
            void itReturnsCreatedWithToken() throws Exception {
                mockMvc.perform(post("/session")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"email\":\"%s\"," +
                                "\"password\":\"%s\"}", EMAIL, PASSWORD)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(".")));
            }

            @Test
            @DisplayName("비밀번호가 틀리다면, BAD REQUEST를 응답한다")
            void itReturnsNotFound() throws Exception {
                mockMvc.perform(post("/session")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"email\":\"%s\"," +
                                "\"password\":\"%s\"}", EMAIL, INVALID_PASSWORD)))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 회원이 없을 경우")
        class ContextWithNotExistedUserEmail {
            @Test
            @DisplayName("NOT FOUND를 응답한다")
            void itReturnsNotFound() throws Exception {
                mockMvc.perform(post("/session")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"email\":\"%s\"," +
                                "\"password\":\"%s\"}", UNKNOWN_EMAIL, PASSWORD)))
                        .andExpect(status().isNotFound());
            }
        }
    }


}
