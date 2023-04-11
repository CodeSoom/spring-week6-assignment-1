package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.PasswordNotMatchedException;
import com.codesoom.assignment.infra.JwtUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@DisplayName("AuthenticationService")
class AuthenticationServiceTest {

    AuthenticationService authenticationService;

    UserService userService = Mockito.mock(UserService.class);
    JwtUtils jwtUtils = Mockito.mock(JwtUtils.class);

    @BeforeEach
    public void init() {
        authenticationService = new AuthenticationService(jwtUtils, userService);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("유저의 비밀번호가 일치하지 않는 경우")
        class context_with_invalid_password {

            @Test
            @DisplayName("PasswordNotMatchedException 얘외를 반환한다.")
            void it_returns_PasswordNotMatchedException() {
                LoginRequestData requestData = LoginRequestData.builder()
                        .email("test123@naver.com")
                        .password("123")
                        .build();

                User user = User.builder()
                        .email("test123@naver.com")
                        .password("errweqrwerqwr")
                        .build();

                given(userService.findByEmail(any())).willReturn(user);

                assertThrows(PasswordNotMatchedException.class, () -> authenticationService.login(requestData));
            }
        }
    }

}
