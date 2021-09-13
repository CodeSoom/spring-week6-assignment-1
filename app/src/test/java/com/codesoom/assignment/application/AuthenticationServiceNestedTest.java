package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginForm;
import com.codesoom.assignment.errors.LoginDataNotMatchedException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codesoom.assignment.Constant.EMAIL;
import static com.codesoom.assignment.Constant.NAME;
import static com.codesoom.assignment.Constant.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthenticationService 클래스")
@SpringBootTest
class AuthenticationServiceNestedTest {
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, jwtUtil);

        setupFixture();
    }

    private void setupFixture() {
        userRepository.deleteAll();
        User user = User.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        userRepository.save(user);
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {
        @Nested
        @DisplayName("회원의 아이디와 비밀번호가 일치한다면")
        class Context_with_valid_data {
            @DisplayName("정상적으로 로그인되어 토큰이 발급된다.")
            @Test
            void login() {
                final String token = authenticationService.login(LoginForm.of(EMAIL, PASSWORD));

                assertThat(token).contains(".");
            }
        }

        @Nested
        @DisplayName("회원의 아이디와 비밀번호가 불일치한다면")
        class Context_with_invalid_data {
            @DisplayName("예외가 발생하며 토큰이 발급되지 않는다.")
            @ParameterizedTest
            @CsvSource(value = {"catsbi@codesoom.co.kr:1234", "test@codesoom.co.kr:q1w2e3", "test:test"}, delimiter = ':')
            void login(String email, String password) {
                assertThatThrownBy(()-> authenticationService.login(LoginForm.of(email, password)))
                        .isInstanceOf(LoginDataNotMatchedException.class)
                        .hasMessage(LoginDataNotMatchedException.DEFAULT_MESSAGE);
            }
        }
    }

}
