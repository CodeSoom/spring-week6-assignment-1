package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.UserNotFoundException;
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

    LoginData testLoginData;

    @BeforeEach
    void setUp() {
        testLoginData = LoginData.builder()
                .email("pjh0819@codesom.com")
                .password("123456")
                .build();
    }

    @Nested
    @DisplayName("login() 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("등록된 유저의 LoginData가 주어진다면")
        class Context_with_LoginData {

            @BeforeEach
            void prepare() {
                User user = User.builder()
                        .email(testLoginData.getEmail())
                        .password(testLoginData.getPassword())
                        .build();

                userRepository.save(user);
            }

            @Test
            @DisplayName("access token을 리턴합니다.")
            void it_return_accessToken() {
                String accessToken = authenticationService.login(testLoginData);

                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 유저의 LoginData가 주어진다면")
        class Context_with_Invaild_LoginData {

            @Test
            @DisplayName("유저를 찾지 못했다는 내용의 예외를 던집니다.")
            void it_return_accessToken() {
                assertThatThrownBy(() -> authenticationService.login(testLoginData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken() 메소드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("accessToken이 주어진다면")
        class Context_with_accessToken {

            Long givenUserid = TEST_USER_ID;

            @Test
            @DisplayName("user id 값을 리턴합니다.")
            void it_return_userId() {
                Long userId = authenticationService.parseToken(VALID_TOKEN);

                assertThat(userId).isEqualTo(givenUserid);
            }
        }
    }
}
