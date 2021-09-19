package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final long TEST_USER_ID = 1L;

    private AuthenticationService service;

    @BeforeEach
    void setup() {
        JwtUtil authenticationUtil = new JwtUtil(SECRET);
        service = new AuthenticationService(authenticationUtil);
    }

    @Test
    @DisplayName("로그인 후 토큰을 반환한다.")
    void loginTest() {
        // when
        String token = service.login(TEST_USER_ID);

        // then
        assertThat(token).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    @DisplayName("토큰을 복호화 하여 UserId를 반환한다.")
    void tokenParseTest() {
        // when
        Long userId = service.parseToken(ACCESS_TOKEN);

        // then
        assertThat(userId).isEqualTo(TEST_USER_ID);
    }
}
