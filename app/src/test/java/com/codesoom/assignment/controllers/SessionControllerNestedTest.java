package com.codesoom.assignment.controllers;

import com.codesoom.assignment.config.TestWebConfig;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginForm;
import com.codesoom.assignment.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.codesoom.assignment.ConstantForAuthenticationTest.EMAIL;
import static com.codesoom.assignment.ConstantForAuthenticationTest.NAME;
import static com.codesoom.assignment.ConstantForAuthenticationTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("SessionController 클래스")
@WebMvcTest(SessionController.class)
@Import(TestWebConfig.class)
class SessionControllerNestedTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        userRepository.deleteAll();

        user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
        user = userRepository.save(user);
        mockSetUp();
    }

    private void mockSetUp() {

    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("이메일이 존재하고, 비밀번호가 일치한다면")
        class Context_with_exists_email_and_matched_password {
            @DisplayName("토큰이 발급되며 201 응답코드를 반환한다.")
            @Test
            void loginWithValidForm() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                                        .content(toJson(LoginForm.of(EMAIL, PASSWORD)))
                        )
                        .andExpect(status().isCreated())
                        .andExpect(content().string(containsString(".")))
                        .andDo((result) -> {
                            final String token = result.getResponse().getContentAsString();
                            final Long decodeId = jwtUtil.decode(token);

                            assertThat(decodeId).isEqualTo(user.getId());
                        });
            }
        }

        @Nested
        @DisplayName("이메일이 존재하고, 비밀번호가 일치하지 않는다면")
        class Context_with_exists_email_and_unmatched_password {
            @DisplayName("예외가 발생하며 401 응답코드를 반환한다.")
            @Test
            void loginWithNotMatchedForm() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                                        .content(toJson(LoginForm.of(EMAIL, "other")))
                        )
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("이메일이 존재하지 않는 경우")
        class Context_with_not_exists_email {
            @DisplayName("예외가 발생하며 401 응답코드를 반환한다.")
            @Test
            void loginWithNotExistsEmail() throws Exception {
                mockMvc.perform(
                                post("/session")
                                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                                        .content(toJson(LoginForm.of("other", PASSWORD)))
                        )
                        .andExpect(status().isNotFound());
            }
        }
    }

    private byte[] toJson(LoginForm form) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(form);
    }

}
