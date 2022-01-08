package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final Long TEST_USER_ID = 1L;

    SessionRequestData vaildSessionRequestData;

    SessionRequestData invalidSessionRequestData;

    SessionRequestData wrongPasswordSessionRequestData;

    User validUser;

    User user;
    String validToken;

    @BeforeEach
    void setUp() {
        vaildSessionRequestData = SessionRequestData.builder()
                .email("pjh0819@codesom.com")
                .password("123456")
                .build();

        validUser = User.builder()
                .email(vaildSessionRequestData.getEmail())
                .password(vaildSessionRequestData.getPassword())
                .build();

        invalidSessionRequestData = SessionRequestData.builder()
                .email("skcldi1234@codesom.com")
                .password("123456")
                .build();

        wrongPasswordSessionRequestData = SessionRequestData.builder()
                .email("pjh0819@codesom.com")
                .password("abcdef")
                .build();

        user = userRepository.save(validUser);

        validToken = jwtUtil.encode(user.getId());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteById(user.getId());
    }

    @Nested
    @DisplayName("login() 메소드는")
    class Describe_login {

        @Test
        @DisplayName("access token을 리턴합니다.")
        void it_return_accessToken() {
            String accessToken = authenticationService.login(vaildSessionRequestData);

            assertThat(accessToken).isEqualTo(validToken);
        }

        @Nested
        @DisplayName("등록되지 않은 유저의 SessionRequestData가 주어진다면")
        class Context_with_invaild_sessionRequestData {

            @Test
            @DisplayName("로그인이 실패했다는 내용의 예외를 던집니다.")
            void it_throw_LoginFailException() {
                assertThatThrownBy(() -> authenticationService.login(invalidSessionRequestData))
                        .isInstanceOf(LoginFailException.class);
            }
        }

        @Nested
        @DisplayName("password가 일치하지 않는 SessionRequestData가 주어진다면")
        class Context_with_wrong_password_sessionRequestData {

            @Test
            @DisplayName("로그인이 실패했다는 내용의 예외를 던집니다.")
            void it_throw_LoginFailException() {
                assertThatThrownBy(() -> authenticationService.login(invalidSessionRequestData))
                        .isInstanceOf(LoginFailException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken() 메소드는")
    class Describe_parseToken {

        @Test
        @DisplayName("user id을 리턴합니다.")
        void it_return_userId() {
            Long userId = authenticationService.parseToken(VALID_TOKEN);

            assertThat(userId).isEqualTo(TEST_USER_ID);
        }
    }
}
