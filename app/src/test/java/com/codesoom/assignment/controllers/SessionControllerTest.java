package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
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

        @BeforeEach
        void prepare_jwt() {
            given(authenticationService.login(any(SessionRequestData.class)))
                    .willReturn("qwe.asd.zxc");
        }

        @Test
        @DisplayName("유효한 JWT를 리턴한다")
        void It_returns_jwt() throws Exception {
            final SessionRequestData sessionRequestData =
                    SessionRequestData.builder()
                                      .email("markruler@codesoom.com")
                                      .password("test")
                                      .build();

            mockMvc.perform(post("/session")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(sessionRequestData)))
                   .andExpect(status().isCreated())
                   .andExpect(jsonPath("$.accessToken",
                                       matchesRegex("(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)")));
        }
    }
}
