package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginDto;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789012";

    private AuthenticationService authenticationService;

    private final UserRepository userRepository = mock(UserRepository.class);

    private UserLoginDto userLoginDto;
    private User user;
    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(userRepository, jwtUtil);

        user = User.builder()
                .id(1L)
                .email("rhfpdk92@naver.com")
                .password("1234")
                .name("양승인")
                .build();
        userLoginDto = UserLoginDto.builder()
                .email("rhfpdk92@naver.com")
                .password("1234")
                .build();
    }

    @Test
    void login() {
        given(userRepository.findByEmail("rhfpdk92@naver.com"))
        .willReturn(Optional.of(user));


        String accessToken = authenticationService.login(userLoginDto);
        assertThat(accessToken).contains(".");
    }
}
