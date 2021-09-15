package com.codesoom.assignment.controllers;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    private static final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$";

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
            .id(1L)
            .email(EMAIL)
            .password(PASSWORD)
            .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(
            post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(LoginRequestDto.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build()))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken", matchesPattern(JWT_REGEX)));
    }

    private String toJson(LoginRequestDto user) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(user);
    }
}
