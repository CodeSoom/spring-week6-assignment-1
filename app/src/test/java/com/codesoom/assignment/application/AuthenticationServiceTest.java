package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String HEADER = "eyJhbGciOiJIUzI1NiJ9";
    private static final String PAYLOAD= "eyJ1c2VySWQiOjF9";
    private static final String SIGNATURE = "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_SIGNATURE = "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDM" + "INVALID";

    private static final String VALID_TOKEN = String.join(".", HEADER, PAYLOAD, SIGNATURE);
    private static final String INVALID_TOKEN = String.join(".", HEADER, PAYLOAD, INVALID_SIGNATURE);

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        User user = User.builder()
            .password("test")
            .build();
        given(userRepository.findByEmail("tester@example.com")).willReturn(Optional.of(user));
    }

    @DisplayName("서비스에 로그인을 요청하면, 유효한 토큰을 반환한다.")
    @Test
    void loginWithRightEmailAndPassword() {
        String accessToken = authenticationService
            .login("tester@example.com", "test");

        assertThat(accessToken).isEqualTo(VALID_TOKEN);

        verify(userRepository).findByEmail("tester@example.com");
    }

    @DisplayName("서비스에 올바르지 않은 이메일로 로그인을 요청하면, 예외를 던진다.")
    @Test
    void loginWithWrongEmail() {
        assertThatThrownBy(() -> authenticationService.login("badguy@example.com", "test"))
            .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("badguy@example.com");
    }

    @DisplayName("서비스에 올바르지 않은 패스워드로 로그인을 요청하면, 예외를 던진다.")
    @Test
    void loginWithWrongPassword() {
        assertThatThrownBy(() -> authenticationService.login("tester@example.com", "xxx"))
            .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("tester@example.com");
    }

    @DisplayName("서비스에 값이 유효한 토큰 분석을 요청 하면, 유저 아이디를 반환한다.")
    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @DisplayName("서비스에 값이 유효하지 않은 토큰을 분석 요청 하면, 예외를 던진다.")
    @Test
    void parseTokenWithInValidToken() {
        assertThatThrownBy(
            () -> authenticationService.parseToken(INVALID_TOKEN))
            .isInstanceOf(InvalidAccessTokenException.class);
    }

    @DisplayName("서비스에 형식이 올바르지 않은 토큰을 분석 요청 하면, 예외를 던진다.")
    @Test
    void parseTokenWithEmptyToken() {
        assertThatThrownBy(
            () -> authenticationService.parseToken(null))
            .isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(
            () -> authenticationService.parseToken(""))
            .isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(
            () -> authenticationService.parseToken("  "))
            .isInstanceOf(InvalidAccessTokenException.class);
    }
}
