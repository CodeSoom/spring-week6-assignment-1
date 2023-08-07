package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;


@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest extends JpaTest {

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private final String AUTH_NAME = "AUTH_NAME";
    private final String AUTH_EMAIL = "auth@foo.com";
    private final String AUTH_PASSWORD = "12345678";


    private UserLoginData AUTH_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(AUTH_PASSWORD)
            .build();

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class login_메서드는 {
        private AuthenticationService authenticationService;

        @BeforeEach
        void setUp() {
            authenticationService = new AuthenticationService(getUserRepository(), getJwtUtil());
            getUserRepository().deleteAll();
            getUserRepository().save(User.builder()
                    .name(AUTH_NAME)
                    .email(AUTH_EMAIL)
                    .password(AUTH_PASSWORD)
                    .build());
        }

        @DisplayName("유저로그인정보를 받아 인증토큰을 반환한다.")
        @Test
        void It_returns_token() {
            String accessToken = authenticationService.login(AUTH_USER_DATA);
            Assertions.assertThat(accessToken).isNotNull();
            Assertions.assertThat(accessToken).isEqualTo(VALID_TOKEN);
        }
    }
}
