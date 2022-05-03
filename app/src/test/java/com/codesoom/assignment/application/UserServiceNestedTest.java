package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.UserLoginWrongPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("UserService 의")
public class UserServiceNestedTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private final static String VALID_EMAIL = "validUser@google.com";
    private final static String VALID_PASSWORD = "12345678";
    private final static String INVALID_PASSWORD = "123456789";

    private final static String NOT_FOUND_EMAIL = "mr.notfound@google.com";
    private final static String NOT_FOUND_PASSWORD = "mr.notfound@google.com";

    private User validUser;

    @Nested
    @DisplayName("login() 메서드는")
    class Describe_login_method {
        @Nested
        @DisplayName("유효한 회원정보를 받았을 때")
        class Context_with_valid_user {
            public Context_with_valid_user() {
                setUpValidUser();
            }

            @Test
            @DisplayName("JSON Web Token 문자열을 반환한다.")
            void it_returns_jwt() {
                String token = userService.loginUser(VALID_EMAIL, VALID_PASSWORD);
                assertThat(token).isNotEmpty();

                Claims claims = jwtUtil.decode(token);
                Long userId = claims.get("userId", Long.class);
                assertThat(userId).isEqualTo(validUser.getId());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 회원정보를 받았을 때")
        class Context_with_not_found_user {
            @Test
            @DisplayName("UserNotFoundException 이 발생한다.")
            void it_returns_user_not_found_exception() {
                assertThatThrownBy(() -> userService.loginUser(NOT_FOUND_EMAIL, NOT_FOUND_PASSWORD))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("일치하지 않는 비밀번호를 받았을 때")
        class Context_with_wrong_password {
            public Context_with_wrong_password() {
                setUpValidUser();
            }

            @Test
            @DisplayName("UserLoginWrongPasswordException 을 던진다.")
            void it_returns_jwt() {
                assertThatThrownBy(() -> userService.loginUser(VALID_EMAIL, INVALID_PASSWORD))
                        .isInstanceOf(UserLoginWrongPasswordException.class);
            }
        }
    }

    public void setUpValidUser() {
        userRepository.deleteAll();

        validUser = userService.registerUser(UserRegistrationData.builder()
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .name("유효한유저")
                .build());
    }
}
