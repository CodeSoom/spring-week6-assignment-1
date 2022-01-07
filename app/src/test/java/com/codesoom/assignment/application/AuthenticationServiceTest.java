package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginNotMatchPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utills.JwtUtill;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("사용자 권한 서비스")
@DataJpaTest
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";

    private static final String EXIST_EMAIL = "exist@email.com";

    private static final String PASSWORD = "1234";
    private static final String WRONG_PASSWORD = "wrong";

    @Autowired
    private UserRepository userRepository;

    private AuthenticationService authenticationSrvice;

    private User user;
    private Mapper mapper;
    private JwtUtill jwtUtill;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email(EXIST_EMAIL)
                .password(PASSWORD)
                .name("코드숨")
                .build();

        mapper = DozerBeanMapperBuilder.buildDefault();
        jwtUtill = new JwtUtill(SECRET);
        authenticationSrvice = new AuthenticationService(jwtUtill, userRepository);
    }

    @Nested
    @DisplayName("로그인 메소드는")
    class Decribe_login {
        @Nested
        @DisplayName("올바른 사용자 정보가 주어지면")
        class Context_with_correct_user {
            private User givenUser;
            private String validToken;

            @BeforeEach
            void setUp() {
                givenUser = userRepository.save(user);

                validToken = jwtUtill.encode(givenUser.getId());
            }

            @Test
            @DisplayName("인코딩된 엑세스 토큰을 리턴한다.")
            void it_return_accessToken() {
                UserLoginData userLoginData = mapper.map(givenUser, UserLoginData.class);
                String accessToken = authenticationSrvice.login(userLoginData);

                assertThat(accessToken).isEqualTo(validToken);
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 주어지면")
        class Context_with_wrong_password {
            private User givenUser;

            @BeforeEach
            void setUp() {
                givenUser = userRepository.save(user);

                User userWithWrongPassword = User.builder()
                        .password(WRONG_PASSWORD)
                        .build();

                givenUser.changeWith(userWithWrongPassword);
            }

            @Test
            @DisplayName("비밀번호가 일치하지 않다는 예외를 던집니다.")
            void it_throw_LoginNotMatchPasswordException() {
                UserLoginData userLoginData = mapper.map(givenUser, UserLoginData.class);

                assertThatThrownBy(() -> authenticationSrvice.login(userLoginData))
                        .isInstanceOf(LoginNotMatchPasswordException.class);
            }
        }

        @Nested
        @DisplayName("주어진 유저 정보를 찾을 수 없다면")
        class Context_with_not_exists_user {

            @BeforeEach
            void setUp() {
                userRepository.deleteAll();
            }

            @Test
            @DisplayName("사용자를 찾을 수 없다는 예외를 던집니다.")
            void it_throw_() {
                UserLoginData userLoginData = mapper.map(user, UserLoginData.class);

                assertThatThrownBy(() -> authenticationSrvice.login(userLoginData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Decribe_parseToken {
        @Nested
        @DisplayName("유효한 토큰이 들어오면")
        class Context_with_valid_token {
            private String validToken;
            private User givenUser;

            @BeforeEach
            void setUp() {
                givenUser = userRepository.save(user);

                validToken = jwtUtill.encode(givenUser.getId());
            }

            @Test
            @DisplayName("아이디를 디코딩하고 리턴한다.")
            void it_return() {
                Long userId = authenticationSrvice.parseToken(validToken);
                assertThat(userId).isEqualTo(givenUser.getId());
            }
        }

        @Nested
        @DisplayName("인증되지않은 토큰이 들어오면")
        class Context_with_Invalid_token {
            private String invalidToken;

            @BeforeEach
            void setUp() {
                User givenUser = userRepository.save(user);
                String validToken = jwtUtill.encode(givenUser.getId());
                invalidToken = validToken + "000";
            }

            @Test
            @DisplayName("인증되지 않은 토큰이라는 예외를 던진다.")
            void it_return() {
                assertThatThrownBy(
                        () -> authenticationSrvice.parseToken(invalidToken)
                ).isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
