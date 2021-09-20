package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;

class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String ACCESS_INVALID_TOKEN = ACCESS_TOKEN + "invalid";

    AuthenticationService authenticationService;
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {

        jwtUtil = new JwtUtil(SECRET);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);

    }

    @Test
    void login() {

//        String accessToken = authenticationService.login();
//        assertThat(accessToken).contains(".");

    }

    @Test
    void parse() {

        Long userId = authenticationService.parseToken(ACCESS_TOKEN);
        assertThat(userId).isEqualTo(1L);

    }

}

