package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserRegistrationData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.UserNotFoundByEmailException;
import com.codesoom.assignment.utils.JwtUtil;
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
    private static final String SECRET = "01234567890123456789012345678901";
    // TODO: userId: 2, name: 곽형조, email: rhkrgudwh@test.com 인 jwt token
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjIsIm5hbWUiOiLqs73tmJXsobAiLCJlbWFpbCI6InJoa3JndWR3aEB0ZXN0LmNvbSJ9." +
            "oQH48OTpwh08jk1GV9TshGTC7SRJg5wubLrpqop4sG0";
    private static final String INVALID_TOKEN = VALID_TOKEN + "0";

    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    private User existedUser;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil, userService);
    }

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login {

        @Nested
        @DisplayName("email 이 존재하는 user 라면")
        class Context_existed_user_by_email {
            private static final String EXISTED_USER_NAME = "곽형조";
            private static final String EXISTED_USER_EMAIL = "rhkrgudwh@test.com";
            private static final String EXISTED_USER_PASSWORD = "asdqwe1234";

            @BeforeEach
            void prepare() {
                UserRegistrationData registrationData = UserRegistrationData.builder()
                        .name(EXISTED_USER_NAME)
                        .email(EXISTED_USER_EMAIL)
                        .password(EXISTED_USER_PASSWORD)
                        .build();
                existedUser = userService.registerUser(registrationData);
            }

            @Test
            @DisplayName("user 를 찾아 accessToken 을 생성하고 리턴한다")
            void it_return_accessToken() {
                String accessToken = authenticationService.login(existedUser.getEmail());

                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("email 이 존재하지 않는 user 라면")
        class Context_with_not_existed_user_by_email {
            private static final String NOT_EXISTED_USER_NAME = "홍길동";
            private static final String NOT_EXISTED_USER_EMAIL = "ghdrlfehd@test.com";
            private static final String NOT_EXISTED_USER_PASSWORD = "asdqwe1234";

            private User notExistedUser;

            @BeforeEach
            void prepare() {
                UserRegistrationData registrationData = UserRegistrationData.builder()
                        .name(NOT_EXISTED_USER_NAME)
                        .email(NOT_EXISTED_USER_EMAIL)
                        .password(NOT_EXISTED_USER_PASSWORD)
                        .build();
                existedUser = userService.registerUser(registrationData);
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

            @Test
            @DisplayName("parsing 한 결과를 리턴한다")
            void it_return_parsing_token_data() {
                Long userId = authenticationService.parseToken(VALID_TOKEN);

                assertThat(userId).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이라면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}