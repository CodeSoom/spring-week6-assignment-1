package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthenticationService 테스트")
class AuthenticationServiceTest {
    private static final String SECRET = "01234567890123456789012345678901";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9._sqAnLqnuii5tTri0u8AAwGJpI4PF6WRT9wkOLyxWaw";
    private static final String INVALID_TOKEN = VALID_TOKEN + "0";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Test
        @DisplayName("accessToken 을 리턴한다")
        void it_return_accessToken() {
            String accessToken = authenticationService.login();

            assertThat(accessToken).isEqualTo(VALID_TOKEN);
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("유효한 토큰이라면")
        class Context_with_valid_token {

            @Test
            @DisplayName("parsing 한 결과를 리턴한다")
            void it_return_parsing_token_data() {
                Long userId = authenticationService.parseToken(VALID_TOKEN);

                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이라면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}