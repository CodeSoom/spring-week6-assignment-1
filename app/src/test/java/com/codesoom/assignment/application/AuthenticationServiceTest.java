package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest {
    private final String SECRET = "12345678901234567890123456789010";

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private final String INVALID_TOKEN = VALID_TOKEN + "INVALID";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    void login() {
        String accessToken = authenticationService.login(1L);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void parseWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseWithInvalidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @ParameterizedTest(name = "{index}: [{0}]")
    @NullSource
    @EmptySource
    @ValueSource(strings = {" "})
    void parseWithEmptyToken(String input) {
        assertThatThrownBy(() -> authenticationService.parseToken(input))
                .isInstanceOf(InvalidTokenException.class);
    }

}
