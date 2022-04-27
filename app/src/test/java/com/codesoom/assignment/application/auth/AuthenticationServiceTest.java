package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.application.ServiceTest;
import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.domain.users.UserSaveDto;
import com.codesoom.assignment.dto.LoginRequestDto;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest extends ServiceTest {

    private UserLoginService service;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setup() {
        this.service = new UserLoginService(jwtUtil, repository);
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
                private final String USER_EMAIL = "hgd@codesoom.com";
                private final String USER_PASSWORD = "hgdZzang123!";

                private LoginRequestDto LOGIN_REQUEST_DTO= new LoginRequestDto(USER_EMAIL, USER_PASSWORD);

                @BeforeEach
                void setup() {
                    repository.save(new UserSaveDto("홍길동", USER_EMAIL, USER_PASSWORD));
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("성공적으로 토큰을 발급한다.")
                @Test
                void it_return_token() {
                    String access_token = service.login(LOGIN_REQUEST_DTO);

                    assertThat(access_token).contains(".");
                }
            }


            @DisplayName("틀린 비밀번호가 주어지면")
            @Nested
            class Context_with_incorrect_password {
                private final String USER_EMAIL = "hgd@codesoom.com";
                private final String CORRECT_PASSWORD = "hgdZzang123!";
                private final String INCORRECT_PASSWORD = "HiImTroll";

                private final LoginRequestDto LOGIN_REQUEST_DTO = new LoginRequestDto(USER_EMAIL, INCORRECT_PASSWORD);

                @BeforeEach
                void setup() {
                    repository.save(new UserSaveDto("홍길동", USER_EMAIL, CORRECT_PASSWORD));
                }

                @AfterEach
                void cleanup() {
                    repository.deleteAll();
                }

                @DisplayName("예외를 던진다.")
                @Test
                void it_throws_invalid_password() {
                    assertThatThrownBy(() -> service.login(LOGIN_REQUEST_DTO))
                            .isInstanceOf(InvalidPasswordException.class);
                }
            }
        }

        @DisplayName("찾을 수 없는 회원 로그인 정보가 주어지면")
        @Nested
        class Context_with_not_exist_user {

            private final String NOT_EXIST_USER_EMAIL = "ace@codesoom.com";
            private LoginRequestDto LOGIN_REQUEST_DTO = new LoginRequestDto(NOT_EXIST_USER_EMAIL, "12345678");

            @BeforeEach
            void setup() {
                User user = repository.findByEmail(NOT_EXIST_USER_EMAIL).orElse(null);
                if (user != null) {
                    repository.delete(user);
                }
            }

            @DisplayName("예외를 던진다.")
            @Test
            void it_throws_user_not_found() {
                assertThatThrownBy(() -> service.login(LOGIN_REQUEST_DTO))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

}
