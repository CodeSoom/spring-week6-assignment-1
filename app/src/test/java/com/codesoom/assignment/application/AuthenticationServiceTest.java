package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidLoginException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static com.codesoom.assignment.utils.TestHelper.*;


@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest extends JpaTest {



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
                getUserRepository().save(AUTH_USER);
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

            @BeforeEach
            void setUp() {
                getUserRepository().deleteAll();
                getUserRepository().save(AUTH_USER);
            }

            @DisplayName("해당 정보의 회원이 존재하지 않으면 UserNotFoundException을 반환한다.")
            @Test
            void It_throws_UserNotFoundException() {
                Assertions.assertThatThrownBy(() -> authenticationService.login(IS_NOT_EXISTS_USER_DATA)).isInstanceOf(UserNotFoundException.class);
            }

            @DisplayName("비밀번호가 일치하지 않으면 InvalidLoginException을 반환한다.")
            @Test
            void It_throws_InvalidLoginRequest() {
                Assertions.assertThatThrownBy(() -> authenticationService.login(INVALID_PASSWORD_USER_DATA)).isInstanceOf(InvalidLoginException.class);
            }
        }

    }
}
