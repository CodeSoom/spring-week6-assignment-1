package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
        "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
        "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @DisplayName("서비스에 로그인을 요청했을 때, 유효한 토큰을 반환한다.")
    @Test
    void login() {
        String accessToken = authenticationService.login();

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @DisplayName("서비스에 값이 유효한 토큰 분석을 요청했을 때, 유저 아이디를 반환한다.")
    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @DisplayName("서비스에 값이 유효하지 않은 토큰을 분석 요청했을 때, 예외가 호출된다.")
    @Test
    void parseTokenWithInValidToken() {
        assertThatThrownBy(
            () -> authenticationService.parseToken(INVALID_TOKEN))
            .isInstanceOf(InvalidAccessTokenException.class);
    }

    @DisplayName("서비스에 형식이 올바르지 않은 토큰을 분석 요청했을 때, 예외가 호출된다.")
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
