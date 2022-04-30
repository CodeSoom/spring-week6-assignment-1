package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
