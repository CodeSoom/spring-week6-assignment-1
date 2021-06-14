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
    private static final String AUTH_SCHEME = "Bearer";

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
        @DisplayName("찾을 수 있는 유저 정보가 주어지면")
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
        @DisplayName("찾을 수 없는 유저의 로그인 정보가 주어지면")
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
        @DisplayName("올바른 이메일과 틀린 비밀번호를 받았을 때")
        class ContextWithWrongPassword {

            private LoginData invalidLoginData;

            @BeforeEach
            void setUp() {
                invalidLoginData = LoginData.builder()
                        .email(loginData.getEmail())
                        .password(loginData.getPassword() + "as")
                        .build();

                given(userService.getUser(user.getEmail()))
                        .willReturn(user);
            }

            @Test
            @DisplayName("비밀번호가 틀렸다는 예외를 던집니다")
            void ItThrowsWrongPasswordException() {
                assertThatThrownBy(() -> authenticationService.login(invalidLoginData))
                        .isInstanceOf(WrongPasswordException.class);
            }
        }
    }

    @Nested
    @DisplayName("verify 메소드는")
    class DescribeVerify {

        private String validAuthorization;
        private String invalidHeaderAuthorization;
        private String invalidSignatueAuthorization;

        @BeforeEach
        void tokenSetUp() {
            String validToken = jwtUtil.encode(1L);
            String invalidHeaderToken = "invalidHeader" + validToken;
            String invalidSignatueToken = validToken + "invalidSignature";

            validAuthorization = AUTH_SCHEME + " " + validToken;
            invalidHeaderAuthorization = AUTH_SCHEME + " " + invalidHeaderToken;
            invalidSignatueAuthorization = AUTH_SCHEME + " " + invalidSignatueToken;
        }

        @Nested
        @DisplayName("올바른 JWT를 받으면")
        class ContextWithValidToken {

            @Test
            @DisplayName("해독된 정보를 반환합니다")
            void ItReturnsDecoded() {
                assertThat(authenticationService.verify(validAuthorization).getUserId())
                        .isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("Header가 올바르지 않은 토큰을 받으면")
        class ContextWithInvalidHeaderToken {

            @Test
            @DisplayName("올바르지 않은 형식의 JWT에 대한 예외를 던집니다")
            void ItThrowsMalformedJwtException() {
                assertThatThrownBy(() -> authenticationService.verify(invalidHeaderAuthorization))
                        .isInstanceOf(MalformedJwtException.class);
            }
        }

        @Nested
        @DisplayName("Signature가 올바르지 않은 토큰을 받으면")
        class ContextWithInvalidSignatureToken {

            @Test
            @DisplayName("틀린 Signature에 대한 예외를 던집니다")
            void ItThrowsSignatureException() {
                assertThatThrownBy(() -> authenticationService.verify(invalidSignatueAuthorization))
                        .isInstanceOf(SignatureException.class);
            }
        }
    }

}
