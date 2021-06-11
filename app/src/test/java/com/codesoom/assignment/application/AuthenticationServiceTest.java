package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.errors.WrongPasswordException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Nested
@DisplayName("AuthenticationService의")
class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private JwtUtil jwtUtil;

    private AuthenticationService authenticationService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        this.jwtUtil = new JwtUtil(SECRET);
        this.userService = mock(UserService.class);
        this.authenticationService = new AuthenticationService(jwtUtil, this.userService);
    }

    @Nested
    @DisplayName("login 메소드는")
    class DescribeLogin {

        private User user;
        private LoginData loginData;

        @BeforeEach
        void userSetUp() {
            user = User.builder()
                    .name("Jack")
                    .email("jack@email.com")
                    .password("qwer1234")
                    .build();
            loginData = LoginData.builder()
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
        }

        @Nested
        @DisplayName("올바른 유저 인증 정보를 받았을 때")
        class ContextWithValidUserInfo {

            @BeforeEach
            void setUp() {
                given(userService.getUser(user.getEmail())).willReturn(user);
            }

            @Test
            @DisplayName("토큰을 반환합니다")
            void ItReturnsToken() {
                assertThat(authenticationService.login(loginData))
                        .isEqualTo(jwtUtil.encode(user.getId()));
            }
        }

        @Nested
        @DisplayName("유저를 찾을 수 없을 때")
        class ContextWithCannotFindUser {

            @BeforeEach
            void setUp() {
                given(userService.getUser(user.getEmail()))
                        .willThrow(UserNotFoundException.class);
            }

            @Test
            @DisplayName("유저를 찾을 수 없다는 예외를 던집니다")
            void ItThrowsUserNotFoundException() {
                assertThatThrownBy(() -> authenticationService.login(loginData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("틀린 비밀번호를 받았을 때")
        class ContextWithWrongPassword {

            @BeforeEach
            void setUp() {
                given(userService.getUser(user.getEmail()))
                        .willReturn(user);
            }

            @Test
            @DisplayName("비밀번호가 틀렸다는 예외를 던집니다")
            void ItThrowsWrongPasswordException() {
                LoginData invalidLoginData = LoginData.builder()
                        .email(loginData.getEmail())
                        .password(loginData.getPassword() + "as")
                        .build();

                assertThatThrownBy(() -> authenticationService.login(invalidLoginData))
                        .isInstanceOf(WrongPasswordException.class);
            }
        }
    }

    @Nested
    @DisplayName("verify 메소드는")
    class DescribeVerify {

        private final String VALID_TOKEN =
                "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
        private final String INVALID_HEADER_TOKEN =
                "eyJhbGciOiJIUzI1Ni12.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
        private final String INVALID_SIGNATURE_TOKEN =
                "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGas";

        @Nested
        @DisplayName("올바른 JWT를 받으면")
        class ContextWithValidToken {

            @Test
            @DisplayName("해독된 정보를 반환합니다")
            void ItReturnsDecoded() {
                assertThat(authenticationService.verify(VALID_TOKEN).getUserId())
                        .isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("Header가 올바르지 않은 토큰을 받으면")
        class ContextWithInvalidHeaderToken {

            @Test
            @DisplayName("올바르지 않은 형식의 JWT에 대한 예외를 던집니다")
            void ItThrowsMalformedJwtException() {
                assertThatThrownBy(() -> authenticationService.verify(INVALID_HEADER_TOKEN))
                        .isInstanceOf(MalformedJwtException.class);
            }
        }

        @Nested
        @DisplayName("Signature가 올바르지 않은 토큰을 받으면")
        class ContextWithInvalidSignatureToken {

            @Test
            @DisplayName("틀린 Signature에 대한 예외를 던집니다")
            void ItThrowsSignatureException() {
                assertThatThrownBy(() -> authenticationService.verify(INVALID_SIGNATURE_TOKEN))
                        .isInstanceOf(SignatureException.class);
            }
        }
    }

}
