package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRegistrationData;
import com.codesoom.assignment.errors.SessionValidationException;
import com.codesoom.assignment.infra.InMemoryUserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final User TEST_USER = User.builder()
            .email("tester@test.com")
            .name("tester")
            .password("password")
            .build();
    private AuthenticationService authenticationService;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        userRepository = new InMemoryUserRepository();
        authenticationService = new AuthenticationService(jwtUtil, userRepository);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {
        @Nested
        @DisplayName("등록된 유저의 email과 password를 전달했을 때")
        class Context_withRegisteredUserEmailAndPassword {
            SessionRegistrationData registrationData;

            @BeforeEach
            void prepare() {
                userRepository.save(TEST_USER);
                registrationData = new SessionRegistrationData(TEST_USER.getEmail(), TEST_USER.getPassword());
            }

            @Test
            @DisplayName("유요한 토큰을 반환한다")
            void it_returnsValidToken() {
                String result = authenticationService.login(registrationData);
                assertThat(result).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("등록된 유저의 email와 잘못된 password를 전달했을 때")
        class Context_withRegisteredUserEmailAndWrongPassword {
            SessionRegistrationData registrationData;

            @BeforeEach
            void prepare() {
                userRepository.save(TEST_USER);
                registrationData = new SessionRegistrationData(TEST_USER.getEmail(), "wrong password");
            }

            @Test
            @DisplayName("세션 인증에 실패했다는 예외를 던진다")
            void it_throwsSessionValidationException() {
                assertThatThrownBy(()-> { authenticationService.login(registrationData); })
                        .isInstanceOf(SessionValidationException.class);
            }
        }
    }
}
