package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.UserLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserLoginDto userLoginDto;

    @BeforeEach
    void setUp(){
        userLoginDto = UserLoginDto.builder()
                .email("rhfpdk92@naver.com")
                .password("1234")
                .build();
    }

    @Nested
    @DisplayName("POST - /session 요청은")
    class Describe_post_session {

        @Nested
        @DisplayName("로그인하려는 정보가 있고 저장된 유저가 있으면")
        class Context_exist_login_data_and_user{

            @Test
            @DisplayName("응답코드는 201이며 토큰을 응답한다.")
            void it_return_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                        .andDo(print())
                        .andExpect(status().isCreated());
            }
        }
    }
}
