package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestDTO;
import com.codesoom.assignment.errors.LoginFailException;
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

    @Autowired
    private SessionService sessionService;
    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("알맞은 email 과 password 가 주어지면")
        class Context_with_correct_email_and_password {
            private LoginRequestDTO loginRequestDTO;

            @BeforeEach
            void setUp() {
                User savedUser = jpaUserRepository.save(
                        User.builder()
                                .id(1L)
                                .email("a@a.com")
                                .password("123456")
                                .build()
                );
                loginRequestDTO = new LoginRequestDTO(savedUser.getEmail(), savedUser.getPassword());
            }

            @Test
            @DisplayName("token 을 리턴한다")
            void it_returns_token() {
                String token = sessionService.login(loginRequestDTO);

                assertThat(token).isEqualTo(VALID_TOKEN_BY_USER_ID_1L);
            }
        }

        @Nested
        @DisplayName("알맞지 않은 email 과 password 가 주어지면")
        class Context_with_incorrect_email_and_password {
            private LoginRequestDTO loginRequestDTOWithIncorrectEmail;
            private LoginRequestDTO loginRequestDTOWithIncorrectPassword;

            @BeforeEach
            void setUp() {
                User savedUser = jpaUserRepository.save(
                        User.builder()
                                .id(1L)
                                .email("a@a.com")
                                .password("123456")
                                .build()
                );
                loginRequestDTOWithIncorrectEmail = new LoginRequestDTO("b@b.com", savedUser.getPassword());
                loginRequestDTOWithIncorrectPassword = new LoginRequestDTO(savedUser.getEmail(), "9999999");
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 리턴한다")
            void it_returns_token() {
                assertThatThrownBy(
                        () -> sessionService.login(loginRequestDTOWithIncorrectEmail)
                ).isExactlyInstanceOf(LoginFailException.class)
                        .hasMessage("잘못된 email 입니다");

                assertThatThrownBy(
                        () -> sessionService.login(loginRequestDTOWithIncorrectPassword)
                ).isExactlyInstanceOf(LoginFailException.class)
                        .hasMessage("잘못된 password 입니다");
            }
        }
    }
}
