package com.codesoom.assignment.controller.session;

import com.codesoom.assignment.application.auth.InvalidPasswordException;
import com.codesoom.assignment.application.auth.UserLoginService;
import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.application.users.UserSaveRequest;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.dto.TokenResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SessionController 클래스")
@ActiveProfiles("test")
@SpringBootTest
class SessionControllerTest {

    private SessionController controller;

    @Autowired
    private UserLoginService service;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        this.controller = new SessionController(service);
        cleanup();
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @DisplayName("login 메서드는")
    @Nested
    class Describe_login {

        @DisplayName("찾을 수 있는 이메일과")
        @Nested
        class Context_with_exist_user {

            @DisplayName("정확한 비밀번호가 주어지면")
            @Nested
            class Context_with_correct_password {
                private final String EMAIL = "abc@codesoom.com";
                private final String CORRECT_PASSWORD = "abc123correct";

                private final SessionController.LoginRequestDto CORRECT_LOGIN_REQUEST_DTO
                        = new SessionController.LoginRequestDto(EMAIL, CORRECT_PASSWORD);

                @BeforeEach
                void setup() {
                    repository.save(new UserSaveRequest() {
                        @Override
                        public String getName() {
                            return "맛동산";
                        }

                        @Override
                        public String getEmail() {
                            return EMAIL;
                        }

                        @Override
                        public String getPassword() {
                            return CORRECT_PASSWORD;
                        }
                    });
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("토큰을 발급한다.")
                @Test
                void it_return_token () {
                    TokenResponse tokenResponse = controller.login(CORRECT_LOGIN_REQUEST_DTO);

                    assertThat(tokenResponse.getAccessToken()).isNotEmpty();
                }
            }

            @DisplayName("틀린 비밀번호가 주어지면")
            @Nested
            class Context_with_incorrect_password {
                private final String EMAIL = "abc@codesoom.com";
                private final String CORRECT_PASSWORD = "abc123correct";
                private final String INCORRECT_PASSWORD = "abc123incorrect";

                private final SessionController.LoginRequestDto INCORRECT_LOGIN_REQUEST_DTO
                        = new SessionController.LoginRequestDto(EMAIL, INCORRECT_PASSWORD);

                @BeforeEach
                void setup() {
                    repository.save(new UserSaveRequest() {
                        @Override
                        public String getName() {
                            return "맛동산";
                        }

                        @Override
                        public String getEmail() {
                            return EMAIL;
                        }

                        @Override
                        public String getPassword() {
                            return CORRECT_PASSWORD;
                        }
                    });
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("InvalidPassword 예외를 던진다.")
                @Test
                void it_throws_exception () {
                    assertThatThrownBy(() -> controller.login(INCORRECT_LOGIN_REQUEST_DTO))
                            .isInstanceOf(InvalidPasswordException.class);
                }
            }
        }

        @DisplayName("찾을 수 없는 회원 로그인 정보가 주어지면")
        @Nested
        class Context_with_not_exist_user {
            private final String NOT_EXIST_USER_EMAIL = "hola007@fake.email";
            private final SessionController.LoginRequestDto LOGIN_REQUEST_DTO
                    = new SessionController.LoginRequestDto(NOT_EXIST_USER_EMAIL, "fakePWD123");

            @BeforeEach
            void setup() {
                User user = repository.findByEmail(NOT_EXIST_USER_EMAIL).orElse(null);
                if(user != null) {
                    repository.delete(user);
                }
            }

            @DisplayName("UserNotFound 예외를 던진다.")
            @Test
            void it_throws_user_not_found_exception() {
                assertThatThrownBy(() -> controller.login(LOGIN_REQUEST_DTO))
                        .isInstanceOf(UserNotFoundException.class);
            }

        }
    }
}
