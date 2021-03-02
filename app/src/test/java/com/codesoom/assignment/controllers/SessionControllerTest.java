package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    private User validUser;
    private UserLoginDto userLoginDto;

    @BeforeEach
    void setUp(){
        validUser = User.builder()
                .id(1L)
                .name("양승인")
                .password("1234")
                .email("rhfpdk92@naver.com")
                .build();

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

            @BeforeEach
            void setUp(){
                given(userService.validUser(userLoginDto)).willReturn(validUser);
                given(authenticationService.login(validUser)).willReturn(any(String.class));
            }

            @Test
            @DisplayName("응답코드는 201이며 토큰을 응답한다.")
            void it_return_token() throws Exception {
                mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                        .andDo(print())
                        .andExpect(status().isCreated());
                //TODO JWT 토큰 발행로직을 추가한 이후 토큰이 존재한다정도만 추가
            }
        }
    }
}
