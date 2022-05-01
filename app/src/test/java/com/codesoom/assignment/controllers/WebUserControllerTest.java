package com.codesoom.assignment.controllers;

import com.codesoom.assignment.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원에 대한 HTTP 요청")
public class WebUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("/users 를 HTTP 메소드 POST로 요청시")
    class Describe_create {

        @Nested
        @DisplayName("비밀번번호가 연속되는 3개의 숫자를 갖는다면")
        class Context_ConsecutiveNumbersInPassword {

            final String contentTemplate =
                    "{\"email\":\"test1er@example.com\", \"name\":\"Tester\",\"password\":\"%s\"}";

            @BeforeEach
            void setUp() {
                userRepository.deleteAll();
            }

            @ParameterizedTest(name = "(\"{0}\") - HTTP 상태코드 400 을 응답한다.")
            @ValueSource(strings = {"1234", "a789", "567a", "aaaa123", "1a2a3a567a", "a789a" , "1245699"})
            void it_response_400(String password) throws Exception {

                final String requestContent = String.format(contentTemplate, password);

                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestContent)
                        )
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
