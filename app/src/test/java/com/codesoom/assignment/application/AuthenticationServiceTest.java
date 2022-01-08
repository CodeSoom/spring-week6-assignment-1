package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.LoginFailException;
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

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final Long TEST_USER_ID = 1L;

    SessionRequestData testSessionRequestData;

    SessionRequestData wrongPasswordSessionRequestData;

    @BeforeEach
    void setUp() {
        testSessionRequestData = SessionRequestData.builder()
                .email("pjh0819@codesom.com")
                .password("123456")
                .build();

        wrongPasswordSessionRequestData = SessionRequestData.builder()
                .email("pjh0819@codesom.com")
                .password("abcdef")
                .build();
    }

    @Nested
    @DisplayName("login() 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("정상적인 SessionRequestData 파라미터가 주어진다면 ")
        class Context {

            @BeforeEach
            void prepare() {
                User user = User.builder()
                        .email(testSessionRequestData.getEmail())
                        .password(testSessionRequestData.getPassword())
                        .build();

                userRepository.save(user);
            }

            @Test
            @DisplayName("access token을 리턴합니다.")
            void it_return_accessToken() {
                String accessToken = authenticationService.login(testSessionRequestData);

                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 유저의 SessionRequestData가 주어진다면")
        class Context_with_Invaild_SessionRequestData {

            @Test
            @DisplayName("로그인이 실패했다는 내용의 예외를 던집니다.")
            void it_throw_LoginFailException() {
                assertThatThrownBy(() -> authenticationService.login(testSessionRequestData))
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
