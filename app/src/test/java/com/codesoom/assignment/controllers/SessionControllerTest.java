package com.codesoom.assignment.controllers;

import com.codesoom.assignment.TestUtil;
import com.codesoom.assignment.dto.AuthData;
import com.codesoom.assignment.dto.SessionResponse;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.dto.UserResultData;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {
    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String PASSWORD = "password";

    private UserResultData createUser(String email, String password) throws Exception {
        UserRegistrationData userData = UserRegistrationData.builder()
                .email(email)
                .password(password)
                .name("nana")
                .build();

        ResultActions actions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData)));

        return TestUtil.content(actions, UserResultData.class);
    }

    private void deleteUser(Long id) throws Exception {
        mockMvc.perform(delete("/users/" + id));
    }

    private String getNotDuplicatedEmail() {
        return System.currentTimeMillis() + "test.com";
    }

    @DisplayName("With a correct user")
    @Nested
    class WithCorrectUser {
        AuthData authData;

        @BeforeEach
        void setup() throws Exception {
            UserResultData user = createUser(getNotDuplicatedEmail(), PASSWORD);

            authData = AuthData.builder()
                    .email(user.getEmail())
                    .password(PASSWORD)
                    .build();
        }

        @DisplayName("responses with a token")
        @Test
        void responsesWithToken() throws Exception {
            ResultActions actions = mockMvc.perform(post("/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authData)))
                    .andExpect(status().isCreated());

            SessionResponse response = TestUtil.content(actions, SessionResponse.class);

            assertThat(TestUtil.isJwt(response.getAccessToken())).isTrue();
        }
    }

    @DisplayName("With wrong password")
    @Nested
    class WithWrongPassword {
        AuthData authData;

        @BeforeEach
        void setup() throws Exception {
            UserResultData user = createUser(getNotDuplicatedEmail(), PASSWORD);

            authData = AuthData.builder()
                    .email(user.getEmail())
                    .password("wrong password")
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

        @BeforeEach
        void setup() throws Exception {
            UserResultData user = createUser(getNotDuplicatedEmail(), PASSWORD);
            deleteUser(user.getId());

            authData = AuthData.builder()
                    .email(user.getEmail())
                    .password(PASSWORD)
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
