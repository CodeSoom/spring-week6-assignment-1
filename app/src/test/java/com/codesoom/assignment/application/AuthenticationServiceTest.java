package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private static final Long testUserId = 1L;

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
        @DisplayName("LoginData가 주어진다면")
        class Context_with_LoginData {

            LoginData givenLoginData;

            @BeforeEach
            void prepare() {
                givenLoginData = LoginData.builder()
                        .email(testLoginData.getEmail())
                        .password(testLoginData.getPassword())
                        .build();

                User user = User.builder()
                        .email(givenLoginData.getEmail())
                        .password(givenLoginData.getPassword())
                        .build();

                userRepository.save(user);
            }

            @Test
            @DisplayName("access token을 리턴합니다.")
            void it_return_accessToken() {
                String accessToken = authenticationService.login(givenLoginData);

                System.out.println("accessToken: " + accessToken);

                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("parseToken() 메소드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("accessToken이 주어진다면")
        class Context_with_accessToken {

            Long givenUserid = testUserId;

            @Test
            @DisplayName("user id 값을 리턴합니다.")
            void it_return_userId() {
                Long userId = authenticationService.parseToken(VALID_TOKEN);

                System.out.println(userId);

                assertThat(userId).isEqualTo(givenUserid);
            }
        }
    }
}
