package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.InvalidUserDataException;
import com.codesoom.assignment.utils.JwtUtil;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

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

            @ParameterizedTest(name = "{displayName}: [{index}] {argumentsWithNames}")
            @DisplayName("토큰이 유효하지 않다는 예외를 던진다")
            @AutoSource
            void parseTokenWithInvalidToken(String invalidPostfix) {
                assertThatThrownBy(() -> authenticationService.parseToken(
                        "Bearer " + VALID_TOKEN + invalidPostfix))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("만약 빈 토큰이 주어진다면")
        class Context_with_blank_token {

            @ParameterizedTest(name = "{displayName}: [{index}] {argumentsWithNames}")
            @DisplayName("토큰이 유효하지 않다는 예외를 던진다")
            @NullAndEmptySource // null, ""
            @ValueSource(strings = {"   ", "\t", "\n"})
            void parseTokenWithBlankToken(String input) {
                assertThatThrownBy(() -> authenticationService.parseToken(input))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }

    @Nested
    @DisplayName("login 메서드는")
    class Describe_login {

        @Nested
        @DisplayName("요청한 정보의 사용자를 찾을 수 있다면")
        class Context_with_existed_user_data {

            @BeforeEach
            void prepare_request_data() {
                given(userService.findUserByEmailAndPassword(anyString(), anyString()))
                        .willReturn(User.builder()
                                        .id(1L)
                                        .build());
            }

            @ParameterizedTest(name = "{displayName}: [{index}] {argumentsWithNames}")
            @DisplayName("유효한 JWT를 리턴한다")
            @AutoSource
            void It_returns_jwt(String email, String password) {
                final SessionRequestData sessionRequestData =
                        SessionRequestData.builder()
                                          .email(email)
                                          .password(password)
                                          .build();

                String token = authenticationService.login(sessionRequestData);

                assertThat(token).contains(".");
            }
        }

        @Nested
        @DisplayName("요청한 정보의 사용자를 찾을 수 없다면")
        class Context_with_not_existed_user_data {

            @BeforeEach
            void prepare_request_data() {
                given(userService.findUserByEmailAndPassword(anyString(), anyString()))
                        .willThrow(InvalidUserDataException.class);
            }

            @ParameterizedTest(name = "{displayName}: [{index}] {argumentsWithNames}")
            @DisplayName("사용자 정보가 옳지 않다는 예외를 던진다")
            @AutoSource
            void It_throws_invalid_user_data_exception(String email,
                                                       String password) {
                final SessionRequestData sessionRequestData =
                        SessionRequestData.builder()
                                          .email(email)
                                          .password(password)
                                          .build();

                assertThatThrownBy(() -> authenticationService.login(sessionRequestData))
                        .isInstanceOf(InvalidUserDataException.class);

                verify(userService).findUserByEmailAndPassword(anyString(),
                                                               anyString());
            }
        }
    }
}
