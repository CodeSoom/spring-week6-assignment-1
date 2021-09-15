package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    private final UserService userService = mock(UserService.class);

    private static final String secret = "12345678901234567890123456789010";

    private static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5qbdTrYLuxHeNPvUdPmExYWv25gk7BwSjhAoPgoIvaA";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(secret);
        authenticationService = new AuthenticationService(jwtUtil, userService);
        given(authenticationService.login(any(LoginRequestData.class))).willReturn(token);
    }

    @Test
    void login() {
        LoginRequestData loginRequestData = LoginRequestData.builder()
                .email("tester@email.com")
                .password("1234")
                .build();

        String accessToken = authenticationService.login(loginRequestData);

        assertThat(accessToken).isEqualTo(token);
    }

}