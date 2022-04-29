package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.infra.JpaUserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthenticationService 클래스")
@DataJpaTest
class AuthenticationServiceTest {

    private static final String SECRET = "12345678901234567890123456789012";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "12345";

    private AuthenticationService authenticationService;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, jpaUserRepository);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @BeforeEach
        void setup() {
            User registrationData = User.builder()
                .email(EMAIL)
                .name("테스트 이름")
                .password(PASSWORD)
                .build();
            jpaUserRepository.save(registrationData);
        }

        @AfterEach
        void cleanup() {
            jpaUserRepository.deleteAll();
        }

        @Nested
        @DisplayName("이메일과 비밀번호가 일치하는 사용자가 존재한다면")
        class Context_with_a_valid_user {
            SessionRequestData sessionRequestData = new SessionRequestData(EMAIL, PASSWORD);

            @Test
            @DisplayName("토큰을 생성하여 반환한다.")
            void it_returns_a_token() {
                String accessToken = authenticationService.login(sessionRequestData);
                assertThat(accessToken).contains(".");
            }
        }

        @Nested
        @DisplayName("아이디가 사용자 목록에 존재하지 않는다면")
        class Context_with_invalid_email {

            SessionRequestData sessionRequestData = new SessionRequestData("not existed " + EMAIL, PASSWORD);

            @Test
            @DisplayName("예외를 던진다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> authenticationService.login(sessionRequestData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("일치하지 않는 비밀번호를 입력한다면")
        class Context_with_invalid_password {

            SessionRequestData sessionRequestData = new SessionRequestData(EMAIL, PASSWORD + "123");

            @Test
            @DisplayName("예외를 던진다.")
            void it_returns_exception() {
                assertThatThrownBy(() -> authenticationService.login(sessionRequestData))
                        .isInstanceOf(InvalidPasswordException.class);
            }
        }
    }
}