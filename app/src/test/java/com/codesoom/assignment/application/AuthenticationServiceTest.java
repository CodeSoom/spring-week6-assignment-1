package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginRequestDto;
import com.codesoom.assignment.errors.UserNotAuthenticatedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthenticationServiceTest {

    private static final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$";

    private static final String EMAIL = "sprnd645@gmail.com";
    private static final String PASSWORD = "password";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    private LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {
        loginRequestDto = LoginRequestDto.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build();

        userRepository.save(User.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    class Describe_authenticate {

        @Nested
        @DisplayName("인증된 회원 정보를 갖는 로그인 요청이 주어지면")
        class Context_authenticatedUserLoginRequest {

            @Test
            @DisplayName("액세스 토큰을 리턴한다")
            void it_returns_accessToken() {
                String accessToken = authenticationService.authenticate(loginRequestDto)
                    .getAccessToken();

                System.out.println("*** AccessToken: " + accessToken);

                assertThat(Pattern.matches(JWT_REGEX, accessToken)).isTrue();
            }
        }

        @Nested
        @DisplayName("삭제된 회원의 정보를 갖는 로그인 요청이 주어지면")
        class Context_deletedUserLoginRequest {

            @BeforeEach
            void setUp() {
                userRepository.deleteAll();
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws() {
                assertThatThrownBy(() -> {
                    authenticationService.authenticate(loginRequestDto);
                }).isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("인증되지 않은 회원의 정보를 갖는 로그인 요청이 주어지면")
        class Context_notAuthenticatedUserLoginRequest {

            @BeforeEach
            void setUp() {
                loginRequestDto = LoginRequestDto.builder()
                    .email(EMAIL)
                    .password("NOT_MATCH_PASSWORD")
                    .build();
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws() {
                assertThatThrownBy(() -> {
                    authenticationService.authenticate(loginRequestDto);
                }).isInstanceOf(UserNotAuthenticatedException.class);
            }
        }
    }
}
