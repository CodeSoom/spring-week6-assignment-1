package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private final String EMAIL = "codesoom@gmail.com";
    private final String PASSWORD = "1234";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil);

        user = User.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login{
        @Nested
        @DisplayName("user을 로그인처리하고")
        class Context_validUserId{

            @Test
            @DisplayName("토큰을 리턴한다.")
            void it_return_token(){
                String accessToken = authenticationService.login();

                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 들어오면")
        class Describe_wrong_password{

            @BeforeEach
            void setUp(){
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_return_exception(){
            }
        }
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void parseTokenWithBlankToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken(null))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken("   "))
                .isInstanceOf(InvalidTokenException.class);
    }
}
