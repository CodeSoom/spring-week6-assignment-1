package com.codesoom.assignment.controllers;

import com.codesoom.assignment.annotations.Utf8MockMvc;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Utf8MockMvc
@DisplayName("/session 경로는")
class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Nested
    @DisplayName("POST 요청을 받았을 때")
    class Describe_post_request {
        private final MockHttpServletRequestBuilder requestBuilder;

        public Describe_post_request() {
            requestBuilder = post("/session");
        }

        @Nested
        @DisplayName("유효한 email 과 password 를 받는다면")
        class Context_valid_email_and_password {
            private final String validEmail = "abc@abc.com";
            private final String validPassword = "abcdefg";
            private final ResultActions actions;

            public Context_valid_email_and_password() throws Exception {
                actions = mockMvc.perform(requestBuilder
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(
                                "{\"email\":%s,\"password\":%s}"
                                , validEmail, validPassword)));
            }

            @Test
            @DisplayName("200 OK 응답을 전달한다.")
            void it_responses_200_ok() throws Exception {
                actions.andExpect(status().isOk());
            }

            @Test
            @DisplayName("JWT 토큰 값을 반환한다.")
            void it_returns_jwt_token() throws Exception {
                // TODO: Secret 테스트용 ID 를 로그인했을 때 실제로 나오는 토큰을 expected 에 넣어준다.
                actions.andExpect(content().string(""));
            }
        }
    }
}