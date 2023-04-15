package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.PasswordNotMatchedException;
import com.codesoom.assignment.infra.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("AuthenticationService ")
class AuthenticationServiceTest {
    AuthenticationService authenticationService;

    UserService userService = Mockito.mock(UserService.class);
    JwtUtils jwtUtils = Mockito.mock(JwtUtils.class);

    final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGV";
    final String BLANK_TOKEN = " ";
    LoginRequestData requestData;
    User user;

    @BeforeEach
    public void init() {
        authenticationService = new AuthenticationService(jwtUtils, userService);

        requestData = LoginRequestData.builder()
                .email("test123@naver.com")
                .password("123")
                .build();

        user = User.builder()
                .email("test123@naver.com")
                .password("errweqrwerqwr")
                .build();
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("유저의 비밀번호가 일치하지 않는 경우")
        class context_with_invalid_password {

            @Test
            @DisplayName("PasswordNotMatchedException 얘외를 던진다.")
            void it_returns_PasswordNotMatchedException() {
                given(userService.findByEmail(any())).willReturn(user);

                assertThatThrownBy(() -> authenticationService.login(requestData)).isInstanceOf(PasswordNotMatchedException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("유효한 토큰이 들어왔을 경우")
        class context_with_valid_token {

            @Test
            @DisplayName("토큰에서 유저아이디를 반환한다.")
            void it_returns_userId() {
                given(jwtUtils.decode(TOKEN)).will(invocation -> {
                    JwtUtils jwtUtils = new JwtUtils("12345678901234567890123456789010");
                    String token = invocation.getArgument(0);
                    return jwtUtils.decode(token);
                });

                assertThat(authenticationService.parseToken(TOKEN)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 들어왔을 경우")
        class context_with_invalid_token {

            @DisplayName("InvalidTokenException 예외를 던진다 ")
            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {BLANK_TOKEN, INVALID_TOKEN})
            void it_returns_InvalidTokenException(String invalidToken) {
                given(jwtUtils.decode(invalidToken)).will(invocation -> {
                    JwtUtils jwtUtils = new JwtUtils("12345678901234567890123456789010");
                    String token = invocation.getArgument(0);
                    return jwtUtils.decode(token);
                });

                assertThatThrownBy(() -> authenticationService.parseToken(invalidToken)).isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
