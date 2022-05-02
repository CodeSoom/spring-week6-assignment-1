package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        given(authenticationService.login(1L)).willReturn("a.b.c");

        User user = User.builder()
                .id(1L)
                .name("john")
                .email("john@gmail.com")
                .password("verysecret")
                .build();
        given(userService.findUserByEmail("john@gmail.com")).willReturn(user);
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@gmail.com\",\"password\":\"verysecret\"}")
                ).andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }

    @Test
    @DisplayName("로그인 테스트 잘못된 비밀번호")
    void loginWithWrongPassword() throws Exception {
        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@gmail.com\",\"password\":\"verysecret!\"}")
                ).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 테스트 등록되지 않은 이메일 주소")
    void loginWithUnregisteredEmail() throws Exception {
        mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john1@gmail.com\",\"password\":\"verysecret!\"}")
        ).andExpect(status().isUnauthorized());
    }
}