package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationService authenticationService;

    private static String VALID_TOKEN = "";

    @BeforeEach
    void setUp(){
        User user = new User();
        user.builder().id(1l);
        VALID_TOKEN = authenticationService.login(user);
    }
    @Test
    void login() throws Exception {
        mockMvc.perform(post("/session")
                        .content("{\"userId\":1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(".")));
    }
    //@Test
    void encode(){
        User user = new User();
        user.builder().id(1l);
        String accessToken = authenticationService.login(user);
        assertThat(accessToken).contains(".");
    }

    //@Test
    void decode(){
        authenticationService.decode(VALID_TOKEN);
    }
}
