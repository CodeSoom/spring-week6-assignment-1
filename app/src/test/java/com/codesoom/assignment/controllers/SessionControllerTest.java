package com.codesoom.assignment.controllers;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.AuthData;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private <T> T getResponseContent(ResultActions actions, TypeReference<T> type)
            throws UnsupportedEncodingException, JsonProcessingException {
        MvcResult mvcResult = actions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(contentAsString, type);
    }

    @DisplayName("With a correct user")
    @Nested
    class WithCorrectUser {
        AuthData authData;
        String email = System.currentTimeMillis() + "test.com";
        String password = "password";

        @BeforeEach
        void createUser() throws Exception {
            UserRegistrationData userData = UserRegistrationData.builder()
                    .email(email)
                    .password(password)
                    .name("nana")
                    .build();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userData)));
        }

        @BeforeEach
        void setupFixtures() {
            authData = AuthData.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

        @DisplayName("responses with a token")
        @Test
        void responsesWithToken() throws Exception {
            mockMvc.perform(post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authData)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString(".")));
            // TODO: 제대로 된 토큰인지 확인하기
        }
    }

    @DisplayName("With wrong password")
    @Nested
    class WithWrongPassword {
        AuthData authData;
        String email = System.currentTimeMillis() + "test.com";
        String password = "password";

        @BeforeEach
        void createUser() throws Exception {
            UserRegistrationData userData = UserRegistrationData.builder()
                    .email(email)
                    .password(password)
                    .name("nana")
                    .build();

            mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userData)));
        }

        @BeforeEach
        void setupFixtures() {
            authData = AuthData.builder()
                    .email(email)
                    .password("wrong-password")
                    .build();
        }

        @DisplayName("responses with HTTP status code 400")
        @Test
        void responsesWith400Error() throws Exception {
            mockMvc.perform(post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authData)))
                    .andExpect(status().isBadRequest());
        }
    }

    @DisplayName("With a non-existent user")
    @Nested
    class WithNonExistentUser {
        AuthData authData;
        String email = System.currentTimeMillis() + "test.com";
        String password = "password";

        @BeforeEach
        void deleteExistentUser() throws Exception {
            UserRegistrationData userData = UserRegistrationData.builder()
                    .email(email)
                    .password(password)
                    .name("nana")
                    .build();

            ResultActions actions = mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userData)));

            User createdUser = getResponseContent(actions, new TypeReference<User>() {});

            mockMvc.perform(delete("/users/" + createdUser.getId()));
        }

        @BeforeEach
        void setupFixtures() {
            authData = AuthData.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

        @DisplayName("responses with HTTP status code 404")
        @Test
        void responsesWith404Error() throws Exception {
            mockMvc.perform(post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authData)))
                    .andExpect(status().isNotFound());
        }
    }
}
