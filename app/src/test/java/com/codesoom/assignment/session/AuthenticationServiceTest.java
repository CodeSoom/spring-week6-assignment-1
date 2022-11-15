package com.codesoom.assignment.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.codesoom.assignment.support.LoginFixture.LOGIN_VALID;
import static org.assertj.core.api.Assertions.assertThat;

/*
    - login 메서드는
        - 유효한 회원 정보가 주어지면
            - 토큰을 반환한다
        - 유효하지 않는 회원 정보가 주어지면
            - 찾을 수 없는 Email일 경우
                - 예외를 던진다 (UserNotFoundException)
            - 비밀번호가 틀렸을 경우
                - 예외를 던진다 (InvalidUserPasswordException)
*/
@DisplayName("AuthenticationService 단위 테스트")
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUpVariable() {
        authenticationService = new AuthenticationService();
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_회원_정보가_주어지면 {
            @Test
            @DisplayName("토큰을 반환한다")
            void it_returns_token() {
                String accessToken = authenticationService.login(LOGIN_VALID.로그인_요청_데이터_생성());

                assertThat(accessToken).isNotBlank();
                // TODO: 실제 유저의 accessToken 값인지 검증하는 로직이 있으면 좋을 것 같음
                assertThat(accessToken).contains(".");
            }
        }
    }
}
