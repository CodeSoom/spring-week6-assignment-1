package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequest;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.infra.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SessionServiceTest {
    private static final String VALID_TOKEN_BY_USER_ID_1L = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw123";

    @Autowired
    private SessionService sessionService;
    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        private String correctEmail;
        private String correctPassword;

        @BeforeEach
        void setUp() {
            correctEmail = "a@a.com";
            correctPassword = "123456";

            jpaUserRepository.save(
                    User.builder()
                            .id(1L)
                            .email(correctEmail)
                            .password(correctPassword)
                            .build()
            );
        }

        @Nested
        @DisplayName("알맞은 email 과 password 가 주어지면")
        class Context_with_correct_email_and_password {
            private LoginRequest loginRequest;

            @BeforeEach
            void setUp() {
                loginRequest = new LoginRequest(correctEmail, correctPassword);
            }

            @Test
            @DisplayName("token 을 리턴한다")
            void it_returns_token() {
                String token = sessionService.login(loginRequest);

                assertThat(token).isEqualTo(VALID_TOKEN_BY_USER_ID_1L);
            }
        }

        @Nested
        @DisplayName("알맞지 않은 email 이 주어지면")
        class Context_with_incorrect_email {
            private LoginRequest loginRequestWithIncorrectEmail;

            @BeforeEach
            void setUp() {
                loginRequestWithIncorrectEmail = new LoginRequest("b@b.com", correctPassword);
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던진다")
            void it_returns_token() {
                assertThatThrownBy(
                        () -> sessionService.login(loginRequestWithIncorrectEmail)
                ).isExactlyInstanceOf(UserNotFoundException.class)
                        .hasMessage("찾을 수 없는 email 입니다");
            }
        }

        @Nested
        @DisplayName("알맞지 않은 password 가 주어지면")
        class Context_with_incorrect_password {
            private LoginRequest loginRequestWithIncorrectPassword;

            @BeforeEach
            void setUp() {
                loginRequestWithIncorrectPassword = new LoginRequest(correctEmail, "9999999");
            }

            @Test
            @DisplayName("로그인이 실패했다는 예외를 던진다")
            void it_returns_token() {
                assertThatThrownBy(
                        () -> sessionService.login(loginRequestWithIncorrectPassword)
                ).isExactlyInstanceOf(LoginFailException.class)
                        .hasMessage("잘못된 password 입니다");
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {
        @Nested
        @DisplayName("유효한 token 이 주어진다면")
        class Context_with_valid_token {
            private Long userId;

            @BeforeEach
            void setUp() {
                userId = 1L;
                jpaUserRepository.save(
                        User.builder()
                                .id(userId)
                                .email("a@a.com")
                                .password("123456")
                                .build()
                );
            }

            @Test
            @DisplayName("userId를 리턴한다")
            void it_returns_userId() {
                Long userId = sessionService.parseToken(VALID_TOKEN_BY_USER_ID_1L);

                assertThat(userId).isEqualTo(userId);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 token 이 주어진다면")
        class Context_with_invalid_token {
            @Test
            @DisplayName("유효하지 않은 토큰이라는 예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(
                        () -> sessionService.parseToken(INVALID_TOKEN)
                ).isExactlyInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
