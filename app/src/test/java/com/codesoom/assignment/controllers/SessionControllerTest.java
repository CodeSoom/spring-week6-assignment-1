package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.uitls.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        given(userService.findUserByEmail("test@test.com"))
                .will(invocation -> {
                    User user = invocation.getArgument(0);
                    return User.builder()
                            .id(1L)
                            .email("test@test.com")
                            .name("test")
                            .password("password")
                            .build();
        });
        given(authenticationService.login(any())).willReturn("a.b.c");
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(
                post("/session")
                        .content("{\"email\":\"test@test.com\",\"password\":\"password\"}")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }
}
