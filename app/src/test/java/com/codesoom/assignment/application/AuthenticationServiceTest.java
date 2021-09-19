package com.codesoom.assignment.application;

import static com.codesoom.assignment.domain.UserRepositoryTest.INVALID_EMAIL;
import static com.codesoom.assignment.domain.UserTest.INVALID_PASSWORD;
import static com.codesoom.assignment.domain.UserTest.USER;
import static com.codesoom.assignment.utils.JwtUtilTest.SECRET;
import static org.assertj.core.api.Assertions.assertThat;
import static com.codesoom.assignment.utils.JwtUtilTest.VALID_TOKEN;
import static com.codesoom.assignment.utils.JwtUtilTest.INVALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.dto.LoginInfoData;
import com.codesoom.assignment.errors.InvalidEmailException;
import com.codesoom.assignment.errors.InvalidPasswordException;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.infra.JpaUserRepository;
import com.codesoom.assignment.utils.JwtUtil;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("AuthenticationService 클래스")
public class AuthenticationServiceTest {
    private static final LoginInfoData LOGIN_INFO_DATA = LoginInfoData.builder()
        .email(USER.getEmail())
        .password(USER.getPassword())
        .build();

    private final JwtUtil jwtUtil;
    private final JpaUserRepository jpaUserRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceTest(final JpaUserRepository jpaUserRepository) {
        this.jwtUtil = new JwtUtil(SECRET);
        this.jpaUserRepository = jpaUserRepository;
        this.authenticationService = new AuthenticationService(jwtUtil, jpaUserRepository);
    }

    @BeforeEach
    private void beforeEach() {
        jpaUserRepository.deleteAll();
        jpaUserRepository.save(USER);
    }

    private String subjectLogin(final LoginInfoData loginInfoData) {
        return authenticationService.login(loginInfoData);
    }

    @Nested
    @DisplayName("login 메서드는")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public final class Describe_login {
        private abstract class Context_invalidLogin {
            protected final LoginInfoData loginInfoData;

            protected Context_invalidLogin(final String email, final String password) {
                loginInfoData = LoginInfoData.builder()
                    .email(email)
                    .password(password)
                    .build();
            }

            protected abstract Class<?> subjectException();

            @Test
            @DisplayName("InvalidLoginInfoException을 던진다.")
            public void it_throws_invalid_login_info_exception() {
                assertThatThrownBy(() -> subjectLogin(loginInfoData))
                    .isInstanceOf(subjectException());
            }
        }

        @Nested
        @DisplayName("이메일에 해당하는 유저가 없는 경우")
        public final class Context_invalidEmail extends Context_invalidLogin {
            public Context_invalidEmail() {
                super(INVALID_EMAIL, USER.getPassword());
            }

            @Override
            protected Class<?> subjectException() {
                return InvalidEmailException.class;
            }
        }

        @Nested
        @DisplayName("비밀번호가 잘못된 경우")
        public final class Context_invalidPassword extends Context_invalidLogin {
            public Context_invalidPassword() {
                super(USER.getEmail(), INVALID_PASSWORD);
            }

            @Override
            protected Class<?> subjectException() {
                return InvalidPasswordException.class;
            }
        }

        @Nested
        @DisplayName("유저가 비활성화 된 경우")
        public final class Context_invalidUser extends Context_invalidLogin {
            public Context_invalidUser() {
                super(USER.getEmail(), USER.getPassword());
            }

            @BeforeEach
            private void beforeEach() {
                jpaUserRepository.findByEmail(loginInfoData.getEmail())
                        .ifPresent(user -> {
                            user.destroy();
                            jpaUserRepository.save(user);
                        });
            }

            @Override
            protected Class<?> subjectException() {
                return InvalidPasswordException.class;
            }
        }

        @Test
        @DisplayName("엑세스 토큰을 리턴한다.")
        public void it_returns_access_token() {
            assertThat(subjectLogin(LOGIN_INFO_DATA))
                .isEqualTo(jwtUtil.encode(USER.getId()));
        }
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void parseTokenWithBlankToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(null))
                .isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(() -> authenticationService.parseToken("   "))
                .isInstanceOf(InvalidTokenException.class);
    }
}
