package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginResult;
import com.codesoom.assignment.helper.AuthJwtHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        String secret = "12345678901234567890123456789010";
        AuthJwtHelper authJwtHelper = new AuthJwtHelper(secret);

        authService = new AuthService(authJwtHelper);
    }

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