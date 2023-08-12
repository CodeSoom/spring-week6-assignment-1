package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.codesoom.assignment.utils.TestHelper.AUTH_USER_LOGIN_DATA;
import static com.codesoom.assignment.utils.TestHelper.VALID_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("SessionController 클래스")
class SessionControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AuthenticationService authenticationService;
    @MockBean
    JwtUtil jwtUtil;


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {

        @BeforeEach
        void setUp() {
            given(authenticationService.login(any(UserLoginData.class)))
                    .willReturn(VALID_TOKEN);
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_유저로그인정보_요청를_받으면 {

            @DisplayName("인증토큰을 반환한다.")
            @Test
            void It_returns_token() throws Exception {
                String jsonString = objectMapper.writeValueAsString(AUTH_USER_LOGIN_DATA);

                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").value(VALID_TOKEN));
            }
        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효하지_않은_유저로그인_요청을_받으면 {

            @DisplayName("예외를 반환한다.")
            @ParameterizedTest
            @MethodSource("com.codesoom.assignment.utils.TestHelper#provideInvalidUserLoginRequests")
            void It_returns_exception(UserLoginData loginData) throws Exception {
                String jsonString = objectMapper.writeValueAsString(loginData);

                mockMvc.perform(post("/session")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString))
                        .andExpect(status().isBadRequest());
            }


        }
    }
}
