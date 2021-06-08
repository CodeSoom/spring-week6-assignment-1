package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.invalid";

    private final UserService userService = mock(UserService.class);

    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;

    @BeforeEach
    void set_up() {
        jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(userService, jwtUtil);
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("만약 유효한 토큰이 주어진다면")
        class Context_with_valid_token {

            @Test
            @DisplayName("사용자 식별자를 리턴한다")
            void It_returns_user_id() {
                Long userId = authenticationService.parseToken("Bearer " + VALID_TOKEN);

                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("만약 유효하지 않은 토큰이 주어진다면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("토큰이 유효하지 않다는 예외를 던진다")
            void parseTokenWithInvalidToken() {
                assertThatThrownBy(() -> authenticationService.parseToken("Bearer " + INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("만약 빈 토큰이 주어진다면")
        class Context_with_blank_token {

            @Test
            @DisplayName("토큰이 유효하지 않다는 예외를 던진다")
            void parseTokenWithBlankToken() {
                assertThatThrownBy(() -> authenticationService.parseToken(null))
                        .isInstanceOf(InvalidTokenException.class);

                assertThatThrownBy(() -> authenticationService.parseToken(""))
                        .isInstanceOf(InvalidTokenException.class);

                assertThatThrownBy(() -> authenticationService.parseToken("   "))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {

        @BeforeEach
        void prepare_request_data() {
            given(userService.findUserByEmailAndPassword(anyString(), anyString()))
                    .willReturn(User.builder()
                                    .id(1L)
                                    .build());
        }

        @Test
        @DisplayName("유효한 JWT를 리턴한다")
        void It_returns_jwt() {
            final SessionRequestData sessionRequestData =
                    SessionRequestData.builder()
                                      .email("markruler@codesoom.com")
                                      .password("test")
                                      .build();

            String token = authenticationService.login(sessionRequestData);

            assertThat(token).contains(".");
        }
    }
}
