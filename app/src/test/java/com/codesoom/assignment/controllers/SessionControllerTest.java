package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRegistrationData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.infra.InMemoryUserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SessionControllerTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final User TEST_USER = User.builder()
            .email("tester@test.com")
            .name("tester")
            .password("password")
            .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private UserRepository userRepository;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        final JwtUtil jwtUtil = new JwtUtil(SECRET);
        userRepository = new InMemoryUserRepository();
        final AuthenticationService authenticationService = new AuthenticationService(jwtUtil, userRepository);
        final SessionController controller = new SessionController(authenticationService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Nested
    @DisplayName("POST /session은")
    class Describe_postSession {
        @Nested
        @DisplayName("등록되어 있는 유저 정보를 담아서 요청하면")
        class Context_withExistingUser {
            private SessionRegistrationData sessionRegistrationData;

            @BeforeEach
            void prepare() {
                userRepository.save(TEST_USER);
                sessionRegistrationData = new SessionRegistrationData(TEST_USER.getEmail(), TEST_USER.getPassword());
            }

            @Test
            @DisplayName("Created Status, 유저 정보에 대한 토큰을 반환한다")
            void login() throws Exception {
                SessionResponseData expected = new SessionResponseData(VALID_TOKEN);

                mockMvc.perform(
                        post("/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent(sessionRegistrationData))
                        )
                        .andExpect(status().isCreated())
                        .andExpect(content().json(jsonContent(expected)));
            }
        }
    }

    private String jsonContent(Object value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }
}
