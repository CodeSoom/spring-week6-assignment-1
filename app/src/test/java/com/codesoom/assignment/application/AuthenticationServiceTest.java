package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginNotMatchPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthenticationService 테스트")
@DataJpaTest
class AuthenticationServiceTest {


    @Autowired
    private UserRepository userRepository;

    private Mapper mapper;
    private JwtUtil jwtUtil;

    private static final String SECRET = "12345678901234567890123456789010";
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@test.com")
                .password("password")
                .name("name").build();

        mapper = DozerBeanMapperBuilder.buildDefault();
        jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil, mapper, userRepository);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("login 메서드")
    class Describe_login {

        @Nested
        @DisplayName("올바른 유저 정보가 주어지면")
        class Context_with_correct_user {

            private User givenUser;
            private String valid_token;

            @BeforeEach
            void prepare() {
                givenUser = userRepository.save(user);
                valid_token = jwtUtil.encode(givenUser.getId());
            }

            @Test
            @DisplayName("token을 반환합니다.")
            void it_return_token() {
                UserLoginData userLoginData = mapper.map(givenUser, UserLoginData.class);
                String token = authenticationService.login(userLoginData);

                assertThat(token).isEqualTo(valid_token);
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호를 가진 유저 정보가 주어지면")
        class Context_with_wrong_password {

            private User givenUser;

            @BeforeEach
            void prepare() {
                givenUser = userRepository.save(user);

                User userWithWrongPassword = User.builder().password("wrong password").build();
                givenUser.changeWith(userWithWrongPassword);
            }

            @Test
            @DisplayName("LoginNotMatchPasswordException을 던집니다.")
            void it_throw_LoginNotMatchPasswordException() {
                UserLoginData userLoginData = mapper.map(givenUser, UserLoginData.class);

                assertThatThrownBy(() -> authenticationService.login(userLoginData))
                        .isInstanceOf(LoginNotMatchPasswordException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 유저 정보가 주어지면")
        class Context_with_not_exists_user {

            @BeforeEach
            void prepare() {
                userRepository.deleteAll();
            }

            @Test
            @DisplayName("UserNotFoundException을 던집니다.")
            void it_throw_UserNotFoundException() {
                UserLoginData userLoginData = mapper.map(user, UserLoginData.class);
                assertThatThrownBy(() -> authenticationService.login(userLoginData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("checkToken 메서드")
    class Describe_checkToken {

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_invalid_token {

            private String invalid_token;

            @BeforeEach
            void prepare() {
                User givenUser = userRepository.save(user);
                String valid_token = jwtUtil.encode(givenUser.getId());
                invalid_token = valid_token + "3";
            }

            @Test
            @DisplayName("InvalidAccessToken을 던집니다.")
            void it_throw_invalidAccessToken() {
                assertThatThrownBy(() -> authenticationService.checkToken(invalid_token))
                        .isInstanceOf(InvalidAccessTokenException.class);
            }
        }
    }
}
