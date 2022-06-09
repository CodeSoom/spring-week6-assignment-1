package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.Authentication;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import com.codesoom.assignment.errors.AuthenticationException;
import com.codesoom.assignment.helper.AuthJwtHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    private AuthService authService;

    private static final String secret = "12345678901234567890123456789010";


    @BeforeEach
    void setUp() {
        AuthJwtHelper authJwtHelper = new AuthJwtHelper(secret);

        authService = new AuthService(authJwtHelper, userRepository);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @DataJpaTest
        @Nested
        @DisplayName("유효한 email과 password가 주어지면")
        class Context_with_valid_email_and_password {
            private final LoginData loginData = LoginData.builder()
                    .email("kimchi@naver.com")
                    .password("12345678")
                    .build();

            @BeforeEach
            void setUp() {
                User user = User.builder()
                        .email("kimchi@naver.com")
                        .password("12345678")
                        .build();

                userRepository.save(user);
            }

            @Test
            @DisplayName(".+\\..+\\..+ 패턴의 인증 토큰을 담은 LoginResult를 반환한다.")
            void it_returns_login_result() {
                LoginResult loginResult = authService.login(loginData);

                assertThat(loginResult.getAccessToken())
                        .matches(".+\\..+\\..+");
            }
        }

        @Nested
        @DisplayName("존재하지 않는 email이 주어지면")
        class Context_with_non_existed_email {
            private final LoginData loginData = LoginData.builder()
                    .email("asdasd1@naver.com")
                    .password("12345678")
                    .build();

            @Test
            @DisplayName("AuthenticationException 예외를 던진다.")
            void it_throws_authentication_exception() {
                assertThatThrownBy(() -> authService.login(loginData))
                        .isInstanceOf(AuthenticationException.class);
            }
        }

        @DataJpaTest
        @Nested
        @DisplayName("잘못된 password가 주어지면")
        class Context_with_invalid_password {
            private final LoginData loginData = LoginData.builder()
                    .email("kimchi@naver.com")
                    .password("12345678")
                    .build();

            @BeforeEach
            void setUp() {
                User user = User.builder()
                        .email("kimchi@naver.com")
                        .password("1234567")
                        .build();

                userRepository.save(user);
            }

            @Test
            @DisplayName("AuthenticationException 예외를 던진다.")
            void it_throws_bad_password_exception() {
                assertThatThrownBy(() -> authService.login(loginData))
                        .isInstanceOf(AuthenticationException.class);
            }
        }
    }

    @Nested
    @DisplayName("verify 메소드는")
    class Describe_verify {

        @DataJpaTest
        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {
            private String token;
            private Long savedUserId;

            @BeforeEach
            void setUp() {
                User user = User.builder()
                        .email("kimchi@naver.com")
                        .password("1234567")
                        .build();

                User savedUser = userRepository.save(user);
                savedUserId = savedUser.getId();

                final LoginData loginData = LoginData.builder()
                        .email("kimchi@naver.com")
                        .password("1234567")
                        .build();

                LoginResult loginResult = authService.login(loginData);

                token = loginResult.getAccessToken();
            }

            @Test
            @DisplayName("authentication를 반환함")
            void it_returns_authentication() {
                Authentication authentication = authService.verify(token);

                assertThat(authentication.getId()).isEqualTo(savedUserId);
            }

        }

    }

}
