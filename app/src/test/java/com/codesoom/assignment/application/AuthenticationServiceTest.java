package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.PasswordNotMatchedException;
import com.codesoom.assignment.infra.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("AuthenticationService ")
class AuthenticationServiceTest {
    AuthenticationService authenticationService;

    UserService userService = Mockito.mock(UserService.class);
    JwtUtils jwtUtils = Mockito.mock(JwtUtils.class);

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

                assertThrows(PasswordNotMatchedException.class, () -> authenticationService.login(requestData));
            }
        }
    }
}
