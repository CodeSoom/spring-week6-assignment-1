package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginNotMatchPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("SessionController 테스트")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper;

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private final String EXIST_EMAIL = "test@test.com";
    private final String NOT_EXIST_EMAIL = "notest@test.com";
    private final String VALID_PASSWORD = "password";
    private final String WRONG_PASSWORD = "wrong password";

    private SessionResponseData sessionResponseData;

    private UserLoginData correctUserLoginData;
    private UserLoginData userLoginDataWithWrongPassword;
    private UserLoginData notExistUserLoginData;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        correctUserLoginData = UserLoginData.builder()
                                .email(EXIST_EMAIL)
                                .password(VALID_PASSWORD)
                                .build();

        userLoginDataWithWrongPassword = UserLoginData.builder()
                .email(EXIST_EMAIL)
                .password(WRONG_PASSWORD)
                .build();

        notExistUserLoginData = UserLoginData.builder()
                .email(NOT_EXIST_EMAIL)
                .password(WRONG_PASSWORD)
                .build();

        sessionResponseData = SessionResponseData.builder().accessToken(VALID_TOKEN).build();

        given(authenticationService.login(any(UserLoginData.class))).will(
                invocation -> {
                    UserLoginData userLoginData = invocation.getArgument(0);

                    String email = userLoginData.getEmail();
                    String password = userLoginData.getPassword();

                    if(!email.equals(EXIST_EMAIL)) {
                        throw new UserNotFoundException();
                    }

                    if(!password.equals(VALID_PASSWORD)) {
                        throw new LoginNotMatchPasswordException(email);
                    }

                    return VALID_TOKEN;
                });
    }

    @Nested
    @DisplayName("POST /session 호출")
    class Describe_post_path_session {

        @Nested
        @DisplayName("올바른 유저 정보가 주어지면")
        class Context_with_correct_user {

            @Test
            @DisplayName("status: Created, data: token 을 반환합니다.")
            void it_response_created() throws Exception {
                mockMvc.perform(post("/session")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeContent(correctUserLoginData)))
                        .andExpect(status().isCreated())
                        .andExpect(content().string(makeContent(sessionResponseData)));
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호를 가진 유저 정보가 주어지면")
        class Context_with_wrong_password {

            @Test
            @DisplayName("status: Bad Request를 반환합니다.")
            void it_response_bad_request() throws Exception {
                mockMvc.perform(post("/session")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeContent(userLoginDataWithWrongPassword)))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 유저 정보가 주어지면")
        class Context_with_not_exists_user {

            @Test
            @DisplayName("status: Not Found를 반환합니다.")
            void it_response_not_found() throws Exception {
                mockMvc.perform(post("/session")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeContent(notExistUserLoginData)))
                        .andExpect(status().isNotFound());
            }
        }
    }

    public String makeContent(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
