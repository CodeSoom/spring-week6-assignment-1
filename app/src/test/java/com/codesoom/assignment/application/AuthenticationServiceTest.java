package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;


@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest extends JpaTest {

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private final String AUTH_NAME = "AUTH_NAME";
    private final String AUTH_EMAIL = "auth@foo.com";
    private final String INVALID_EMAIL = AUTH_EMAIL + "INVALID";
    private final String AUTH_PASSWORD = "12345678";


    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {
        private AuthenticationService authenticationService = new AuthenticationService(getUserRepository(), getJwtUtil());


        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효한_유저로그인정보_요청를_받으면 {
            private UserLoginData AUTH_USER_DATA = UserLoginData.builder()
                    .email(AUTH_EMAIL)
                    .password(AUTH_PASSWORD)
                    .build();

            @BeforeEach
            void setUp() {
                getUserRepository().deleteAll();
                getUserRepository().save(User.builder()
                        .name(AUTH_NAME)
                        .email(AUTH_EMAIL)
                        .password(AUTH_PASSWORD)
                        .build());
            }

            @DisplayName("인증토큰을 반환한다.")
            @Test
            void It_returns_token() {
                String accessToken = authenticationService.login(AUTH_USER_DATA);
                Assertions.assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 유효하지_않은_로그인정보를_받으면 {
            private UserLoginData IS_NOT_EXISTS_USER_DATA = UserLoginData.builder()
                    .email(INVALID_EMAIL)
                    .password(AUTH_PASSWORD).build();

            @DisplayName("해당 정보의 회원이 존재하지 않으면 UserNotFoundException을 반환한다.")
            @Test
            void It_throws_UserNotFoundException() {
                Assertions.assertThatThrownBy(() -> authenticationService.login(IS_NOT_EXISTS_USER_DATA)).isInstanceOf(UserNotFoundException.class);
            }
        }

    }
}
