package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidUserDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesRegex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("SessionController 클래스")
class SessionControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    // @SpyBean // 실제 Spy 클래스 필요
    @MockBean // mocking을 통해 무엇을 리턴할지 given에서 지정할 수 있음
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("POST /session")
    class Describe_post_session {

        @Nested
        @DisplayName("요청한 정보의 사용자를 찾을 수 있다면")
        class Context_with_existed_user_data {
            @BeforeEach
            void prepare_jwt() {
                given(authenticationService.login(any(SessionRequestData.class)))
                        .willReturn("qwe.asd.zxc");
            }

            @ParameterizedTest(name = "{displayName}: [{index}] {argumentsWithNames}")
            @DisplayName("유효한 JWT를 응답한다")
            @AutoSource
            void It_returns_jwt(String email, String password) throws Exception {
                final SessionRequestData sessionRequestData =
                        SessionRequestData.builder()
                                          .email(email)
                                          .password(password)
                                          .build();

                mockMvc.perform(post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(sessionRequestData)))
                       .andExpect(status().isCreated())
                       .andExpect(jsonPath("$.accessToken",
                                           matchesRegex("(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)")));
            }
        }

        @Nested
        @DisplayName("요청한 정보의 사용자를 찾을 수 없다면")
        class Context_with_not_existed_user_data {

            @ParameterizedTest(name = "{displayName}: [{index}] {argumentsWithNames}")
            @DisplayName("요청 데이터가 옳지 않다는 상태를 응답한다")
            @AutoSource
            void It_returns_jwt(String email, String password) throws Exception {
                final SessionRequestData sessionRequestData =
                        SessionRequestData.builder()
                                          .email(email)
                                          .password(password)
                                          .build();

                given(authenticationService.login(any(SessionRequestData.class)))
                        .willThrow(new InvalidUserDataException(email));

                mockMvc.perform(post("/session")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(sessionRequestData)))
                       .andExpect(status().isBadRequest())
                       .andExpect(jsonPath("$.message",
                                           containsString("User data is invalid")));
            }
        }
    }
}
