package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.domain.User;
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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@DisplayName("SessionController 클래스")
class SessionControllerTest {
    private final String givenEmail = "juuni.ni.i@gmail.com";
    private final String givenPassword = "secret";
    private final User givenUser = new User(
            1L,
            givenEmail,
            "juunini",
            givenPassword,
            false
    );

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private String generateSignInJSON(String email, String password) {
        return String.format(
                "{\"email\":\"%s\"," +
                        "\"password\":\"%s\"}",
                email, password
        );
    }

    @Nested
    @DisplayName("[POST] /session 요청은")
    class Describe_post_root {
        @Nested
        @DisplayName("주어진 데이터와 일치하는 저장된 유저가 있을 때")
        class Context_with_exists_user_correspond_given_data {
            @BeforeEach
            void setup() {
                given(authService.signIn(givenEmail, givenPassword))
                        .willReturn(givenUser);
            }

            @Test
            @DisplayName("status ok 응답과 함께 토큰을 응답한다.")
            void It_respond_status_ok_and_token() throws Exception {
                mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(generateSignInJSON(givenEmail, givenPassword))
                )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(".")));

                verify(authService).signIn(givenEmail, givenPassword);
            }
        }
    }
}
