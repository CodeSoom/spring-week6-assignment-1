package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private final String SECRET = "12345678901234567890123456789010";

    private final Long USER_ID = 1L;

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private final String INVALID_TOKEN = VALID_TOKEN + "INVALID";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("login 메서드")
    class DescribeLogin {
        @Test
        @DisplayName("회원 식별자에 따른 인증토큰을 반환한다")
        void withUserId() {
            String accessToken = authenticationService.login(USER_ID);

            assertThat(accessToken).isEqualTo(VALID_TOKEN);

            Long userId = authenticationService.parseToken(accessToken);

            assertThat(userId).isEqualTo(USER_ID);
        }
    }

    @Nested
    @DisplayName("parseToken 메서드")
    class DescribeParseToken {
        @Test
        @DisplayName("유효한 토큰이라면 회원 식별자를 반환한다")
        void withValidToken() {
            Long userId = authenticationService.parseToken(VALID_TOKEN);

            assertThat(userId).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("잘못된 토큰이라면 예외를 발생시킨다")
        void withInvalidToken() {
            assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                    .isInstanceOf(InvalidTokenException.class);
        }

        @ParameterizedTest(name = "{index}: <{0}>")
        @NullSource
        @EmptySource
        @ValueSource(strings = {" "})
        @DisplayName("빈 토큰이라면 예외를 발생시킨다")
        void withEmptyToken(String input) {
            assertThatThrownBy(() -> authenticationService.parseToken(input))
                    .isInstanceOf(InvalidTokenException.class);
        }
    }
}
