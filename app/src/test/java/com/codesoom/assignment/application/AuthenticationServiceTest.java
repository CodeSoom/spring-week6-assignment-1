package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.uitls.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private static final String SECRET = "gkskenfdutkeidktjeifnturldksiekt";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3RAdGVzdC5jb20ifQ" +
            ".4HbNGIW9PaXCVuALXZxuqF__XkQYD6BpXndE7Cll4yU";
    private static final String INVALID_TOKEN = VALID_TOKEN.replace('5', '6');
    private static final String TEST_EMAIL = "test@test.com";

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {

        @Nested
        @DisplayName("요청받은 정보를 사용해서")
        class Context_with_valid_email {

            @Test
            @DisplayName("토큰을 생성, 리턴합니다.")
            void it_encode_email() {
                String accessToken = authenticationService.login(TEST_EMAIL);

                assertThat(accessToken).isEqualTo(INVALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("적합한 토큰을 요청받으면")
        class Context_with_valid_token {

            @Test
            @DisplayName("토큰 내의 userEmail을 리턴합니다.")
            void it_return_user_email() {
                String userEmail = authenticationService.parseToken(VALID_TOKEN);

                assertThat(userEmail).isEqualTo(TEST_EMAIL);
            }
        }

        @Nested
        @DisplayName("적합하지 않은 토큰을 요청받으면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("InvalidAccessTokenException을 던집니다.")
            void it_throw_InvalidAccessTokenException() {
                assertThatThrownBy(
                        () -> authenticationService.parseToken(INVALID_TOKEN)
                ).isInstanceOf(InvalidAccessTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰이 비어있거나, Null인 경우")
        class Context_with_blank_token {

            @Test
            @DisplayName("InvalidAccessTokenException을 던집니다.")
            void it_throw_InvalidAccessTokenException() {
                assertThatThrownBy(
                        () -> authenticationService.parseToken("")
                ).isInstanceOf(InvalidAccessTokenException.class);

                assertThatThrownBy(
                        () -> authenticationService.parseToken(null)
                ).isInstanceOf(InvalidAccessTokenException.class);
            }
        }
    }
}
