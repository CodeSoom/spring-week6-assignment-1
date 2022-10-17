package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.request.LoginRequest;
import com.codesoom.assignment.exception.UserNotFoundException;
import com.codesoom.assignment.exception.WrongUserPasswordException;
import com.codesoom.assignment.utils.JwtTestHelper;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {

    private static final String EMAIL = "test@example.com";
    private static final String WRONG_EMAIL = "wrong_test@example.com";
    private static final String PASSWORD = "t!@#$";
    private static final String WRONG_PASSWORD = "wrong_t!@#$";

    @Autowired
    private UserRepository userRepository;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(JwtTestHelper.getSecret());
        UserService userService = new UserService(DozerBeanMapperBuilder.buildDefault(), userRepository);
        authenticationService = new AuthenticationService(jwtUtil, userService);

        userRepository.save(User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build()
        );
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("로그인 가능한 회원 정보가 주어지면")
        class Context_with_valid_user_data {
            private final LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);

            @Test
            @DisplayName("토큰을 반환한다")
            void it_returns_token() {
                String token = authenticationService.login(loginRequest);

                System.out.println(token);
                assertThat(JwtTestHelper.hasPattern(token)).isTrue();
            }
        }

        @Nested
        @DisplayName("회원 정보에 없는 이메일이 주어지면")
        class Context_with_wrong_email {
            private final LoginRequest loginRequest = new LoginRequest(WRONG_EMAIL, PASSWORD);

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> authenticationService.login(loginRequest))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 주어지면")
        class Context_with_wrong_password {
            private final LoginRequest loginRequest = new LoginRequest(EMAIL, WRONG_PASSWORD);

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> authenticationService.login(loginRequest))
                        .isInstanceOf(WrongUserPasswordException.class);
            }
        }
    }
}
