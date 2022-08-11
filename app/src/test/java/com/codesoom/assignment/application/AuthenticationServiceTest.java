package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRegistrationData;
import com.codesoom.assignment.infra.InMemoryUserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void login() {
        userRepository.save(TEST_USER);
        SessionRegistrationData registrationData = new SessionRegistrationData(TEST_USER.getEmail(), TEST_USER.getPassword());
        String accessToken = authenticationService.login(registrationData);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }
}
