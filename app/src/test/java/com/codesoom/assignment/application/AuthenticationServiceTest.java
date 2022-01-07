package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserNotFoundByEmailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthenticationService 테스트")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";

    private static final String EXISTED_USER_NAME = "곽형조";
    private static final String EXISTED_USER_EMAIL = "rhkrgudwh@test.com";
    private static final String EXISTED_USER_PASSWORD = "asdqwe1234";

    private User existedUser;
    private String existedUserToken;

    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private JwtUtil jwtUtil = new JwtUtil(SECRET);

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(jwtUtil, userService);
    }

    void prepareUser() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .name(EXISTED_USER_NAME)
                .email(EXISTED_USER_EMAIL)
                .password(EXISTED_USER_PASSWORD)
                .build();
        existedUser = userService.registerUser(registrationData);

        existedUserToken = jwtUtil.encode(
                existedUser.getId(),
                existedUser.getName(),
                existedUser.getEmail()
        );
    }

    @AfterEach
    void initializeUserRepository() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("email 이 존재하는 user 라면")
        class Context_existed_user_by_email {

            @BeforeEach
            void prepare() {
                prepareUser();
            }

            @Test
            @DisplayName("user 를 찾아 accessToken 을 생성하고 리턴한다")
            void it_return_accessToken() {
                String accessToken = authenticationService.login(existedUser.getEmail());

                assertThat(accessToken).isEqualTo(existedUserToken);
            }
        }

        @Nested
        @DisplayName("email 이 존재하지 않는 user 라면")
        class Context_with_not_existed_user_by_email {
            private User notExistedUser;

            @BeforeEach
            void prepare() {
                prepareUser();
                notExistedUser = userService.deleteUser(existedUser.getId());
            }

            @Test
            @DisplayName("UserNotFoundByEmailException 예외를 던진다")
            void it_return_accessToken() {
                assertThatThrownBy(() -> authenticationService.login(notExistedUser.getEmail()))
                        .isInstanceOf(UserNotFoundByEmailException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Describe_parseToken {

        @Nested
        @DisplayName("유효한 토큰이라면")
        class Context_with_valid_token {

            @BeforeEach
            void prepare() {
                prepareUser();
            }

            @Test
            @DisplayName("parsing 한 결과를 리턴한다")
            void it_return_parsing_token_data() {
                Long userId = authenticationService.parseToken(existedUserToken);

                assertThat(userId).isEqualTo(existedUser.getId());
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이라면")
        class Context_with_invalid_token {

            @BeforeEach
            void prepare() {
                prepareUser();
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> authenticationService.parseToken(existedUserToken + "0"))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}