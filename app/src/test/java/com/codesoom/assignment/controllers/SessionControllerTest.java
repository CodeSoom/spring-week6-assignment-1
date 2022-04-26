package com.codesoom.assignment.controllers;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("로그인 HTTP 요청")
class SessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST - /session ")
    class Describe_signin {

        @Nested
        @DisplayName("유효한 아이디와 패스워드가 주어진다면")
        class Context_valid {
            final User validUser = User.builder()
                    .email("test@naver.com")
                    .password("1234")
                    .build();

            @BeforeEach
            void setUp() {
                userRepository.save(validUser);
            }

            @Test
            @DisplayName("토큰을 생성하고 토큰을 응답합니다. [201] ")
            void it_response_201() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_CBOR)
                                .content("\"email\": \"test@naver.com\", \"password\": \"1234\"")
                        )
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("accessToken").exists());
            }
        }

        @Nested
        @DisplayName("이메일이 존재하지 않다면")
        class Context_notExistEmail {

            @Test
            @DisplayName("Bad Request 를 응답한다. [400]")
            void it_response_400() throws Exception {
                mockMvc.perform(post("/session")
                                .contentType(MediaType.APPLICATION_CBOR)
                                .content("\"email\": \"test@naver.com\", \"password\": \"1234\"")
                        )
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
