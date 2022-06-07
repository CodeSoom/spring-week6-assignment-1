package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("유효한 email과 password가 주어지면")
        class Context_with_valid_email_and_password {
            private final LoginData loginData = LoginData.builder()
                    .email("kimchi@naver.com")
                    .password("12345678")
                    .build();

            @Test
            @DisplayName(".+\\..+\\..+ 패턴의 인증 토큰을 담은 LoginResult를 반환한다.")
            void it_returns_login_result() {
                LoginResult loginResult = authService.login(loginData);

                assertThat(loginResult.getAccessToken())
                        .matches(".+\\..+\\..+");
            }
        }
    }

}