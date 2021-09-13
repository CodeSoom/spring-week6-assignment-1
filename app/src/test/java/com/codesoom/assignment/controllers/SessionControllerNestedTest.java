package com.codesoom.assignment.controllers;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginForm;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static com.codesoom.assignment.Constant.EMAIL;
import static com.codesoom.assignment.Constant.NAME;
import static com.codesoom.assignment.Constant.PASSWORD;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SessionController 클래스")
@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerNestedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        userRepository.deleteAll();

        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        userRepository.save(user);
        mockSetUp();
    }

    private void mockSetUp() {

    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("이메일이 존재하는 경우")
        class Context_with_exists_email {
            @Nested
            @DisplayName("비밀번호가 일치한다면")
            class Context_with_valid_password {
                @DisplayName("토큰이 발급되며 201 응답코드를 반환한다.")
                @Test
                void loginWithValidForm() throws Exception {
                    mockMvc.perform(
                                    post("/session")
                                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                                            .content(toJson(LoginForm.of(EMAIL, PASSWORD)))
                            )
                            .andExpect(status().isCreated())
                            .andExpect(content().string(containsString(".")));
                }
            }

            @Nested
            @DisplayName("비밀번호가 일치하지 않는다면")
            class Context_with_invalid_password {
                @DisplayName("예외가 발생하며 401 응답코드를 반환한다.")
                @Test
                void loginWithNotMatchedForm() throws Exception {
                    mockMvc.perform(
                                    post("/session")
                                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                                            .content(toJson(LoginForm.of(EMAIL, "other")))
                            )
                            .andExpect(status().isUnauthorized());
                }
            }
        }

        @Nested
        @DisplayName("이메일이 존재하지 않는 경우")
        class Context_with_not_exists_email {
            @DisplayName("예외가 발생하며 401 응답코드를 반환한다.")
            @Test
            void loginWithNotExistsEmail() throws Exception{
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                                        .content(toJson(LoginForm.of("other", PASSWORD)))
                        )
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    private byte[] toJson(LoginForm form) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(form);
    }

}
