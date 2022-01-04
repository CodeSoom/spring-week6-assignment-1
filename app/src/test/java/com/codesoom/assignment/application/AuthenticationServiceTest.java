package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utills.JwtUtill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("사용자 권한 서비스")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private AuthenticationService authenticationSrvice;

    @BeforeEach
    void setUp() {
        JwtUtill jwtUtil = new JwtUtill(SECRET);
        authenticationSrvice = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("로그인 메소드는")
    class Decribe_login {
        @Nested
        @DisplayName("로그인 요청자를 로그인 처리하고")
        class Context {
            @Test
            @DisplayName("엑세스 토큰을 리턴한다.")
            void it_return_accessToken() {
                String accessToken = authenticationSrvice.login();
                System.out.println("accessToken" + accessToken);
                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Decribe_parseToken {
        @Nested
        @DisplayName("인증된 토큰이 들어오면")
        class Context_with_valid_token {
            @Test
            @DisplayName("아이디를 디코딩하고 리턴한다.")
            void it_return() {
                Long userId = authenticationSrvice.parseToken(VALID_TOKEN);

                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("인증되지않은 토큰이 들어오면")
        class Context_with_Invalid_token {
            @Test
            @DisplayName("InvalidAccessTokenException 예외를 던진다.")
            void it_return() {
                assertThatThrownBy(
                        () -> authenticationSrvice.parseToken(INVALID_TOKEN)
                ).isInstanceOf(InvalidAccessTokenException.class);
            }
        }

        @Nested
        @DisplayName("비어있는 토큰이 들어오면")
        class Context_with_blank_token {
            @Test
            @DisplayName("InvalidAccessTokenException 예외를 던진다.")
            void it_return() {
                assertThatThrownBy(
                        () -> authenticationSrvice.parseToken("")
                ).isInstanceOf(InvalidAccessTokenException.class);
            }
        }
    }
}
