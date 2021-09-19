package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.codesoom.assignment.utils.JwtUtilTest.VALID_TOKEN;

import com.codesoom.assignment.application.AuthenticationService;

import com.codesoom.assignment.dto.LoginInfoData;
import com.codesoom.assignment.errors.InvalidEmailException;
import com.codesoom.assignment.errors.InvalidPasswordException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach void beforeEach() {
        reset(authenticationService);
    }

    @Nested
    @DisplayName("login 메서드")
    public final class Describe_login {
        @Nested
        @DisplayName("로그인 정보 입력이 없는경우")
        public final class Context_emptyLoginInfo {
            @Test
            @DisplayName("요청이 잘못되었음을 알려준다.")
            public void it_notify_bad_request() throws Exception {
                mockMvc.perform(
                        post("/session")
                    )
                    .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 사용자가 없는경우")
        public final class Context_invalidEmail {
            @BeforeEach
            private void beforeEach() {
                given(authenticationService.login(any(LoginInfoData.class)))
                    .willThrow(new InvalidEmailException());
            }

            @Test
            @DisplayName("이메일에 해당하는 사용자가 없음을 알려준다.")
            public void it_notify_bad_request() throws Exception {
                mockMvc.perform(
                        post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"invalid@example.com\",\"password\":\"test\"}")
                    ).andExpect(status().isNotFound());
            }

            @AfterEach
            private void afterEach() {
                verify(authenticationService)
                    .login(any(LoginInfoData.class));
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않는경우")
        public final class Context_invalidPassword {
            @BeforeEach
            private void beforeEach() {
                given(authenticationService.login(any(LoginInfoData.class)))
                    .willThrow(new InvalidPasswordException());
            }

            @Test
            @DisplayName("비밀번호가 일치하지 않음을 알려준다.")
            public void it_notify_bad_request() throws Exception {
                mockMvc.perform(
                    post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid@example.com\",\"password\":\"test\"}")
                ).andExpect(status().isBadRequest());
            }

            @AfterEach
            private void afterEach() {
                verify(authenticationService)
                    .login(any(LoginInfoData.class));
            }
        }

        @Nested
        @DisplayName("올바른 로그인 정보를 입력한 경우")
        public final class Context_validLoginInfo {
            @BeforeEach
            private void beforeEach() {
                given(authenticationService.login(any(LoginInfoData.class)))
                    .willReturn(VALID_TOKEN);
            }

            @AfterEach
            private void afterEach() {
                verify(authenticationService)
                    .login(any(LoginInfoData.class));
            }

            @Test
            @DisplayName("엑세스 토큰을 발급한다.")
            public void it_issues_an_access_token() throws Exception {
                mockMvc.perform(
                        post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"test@example.com\",\"password\":\"test\"}")
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(VALID_TOKEN)));
            }
        }
    }
}
