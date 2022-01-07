package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.SessionLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginWrongPasswordException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtCodec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final Long TOKEN_USER_ID = 1L;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "abc";

    private final UserService userService = mock(UserService.class);

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login{

        AuthenticationService authenticationService;

        @BeforeEach
        void setUp() {
            JwtCodec jwtCodec = new JwtCodec(SECRET);
            authenticationService = new AuthenticationService(jwtCodec, userService);
            SessionLoginData sessionLoginData = SessionLoginData.builder()
                    .email("exist@example.com")
                    .password("rightPassword")
                    .build();

            given(userService.findUserByEmail(sessionLoginData.getEmail()))
                    .willReturn(
                            User.builder()
                                    .id(1L)
                                    .email(sessionLoginData.getEmail())
                                    .password(sessionLoginData.getPassword())
                                    .build());

            given(userService.findUserByEmail("notExist@example.com"))
                    .willThrow(new UserNotFoundException("notExist@example.com"));
        }

        @Nested
        @DisplayName("존재하는 id와 올바른 pw가 주어지면")
        class Context_with_a_exist_id_and_right_pw {

            SessionLoginData sessionLoginData;
            @BeforeEach
            void setUp(){
                sessionLoginData = SessionLoginData.builder()
                        .email("exist@example.com")
                        .password("rightPassword")
                        .build();
            }

            @Test
            @DisplayName("액세스 토큰을 리턴한다.")
            void login() {
                String accessToken = authenticationService.login(sessionLoginData);
                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("존재하는 id와 잘못된 pw가 주어지면")
        class Context_with_a_exist_id_and_wrong_pw{
            SessionLoginData sessionLoginData;
            @BeforeEach
            void setUp(){
                sessionLoginData = SessionLoginData.builder()
                        .email("exist@example.com")
                        .password("wrongPassword")
                        .build();
            }

            @Test
            @DisplayName("LoginWrongPasswordException 예외를 던진다.")
            void login() {
                assertThatThrownBy(() -> authenticationService.login(sessionLoginData))
                        .isInstanceOf(LoginWrongPasswordException.class);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id가 주어지면")
        class Context_with_a_not_exist_id{
            SessionLoginData sessionLoginData;
            @BeforeEach
            void setUp(){
                sessionLoginData = SessionLoginData.builder()
                        .email("notExist@example.com")
                        .password("rightPassword")
                        .build();
            }

            @Test
            @DisplayName("UserNotFoundException 예외를 던진다.")
            void login() {
                assertThatThrownBy(() -> authenticationService.login(sessionLoginData))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Describe_parse_token{
        
        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_a_valid_token{

            AuthenticationService authenticationService;

            @BeforeEach
            void setUp(){
                JwtCodec jwtCodec = new JwtCodec(SECRET);
                authenticationService = new AuthenticationService(jwtCodec, userService);
            }

            @Test
            @DisplayName("토큰을 디코딩한 id를 리턴한다.")
            void it_returns_decoded_id(){
                Long userId = authenticationService.parseToken(VALID_TOKEN);
                assertThat(userId).isEqualTo(TOKEN_USER_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_a_invalid_token{

            AuthenticationService authenticationService;
            UserService userService;

            @BeforeEach
            void setUp(){
                JwtCodec jwtCodec = new JwtCodec(SECRET);
                authenticationService = new AuthenticationService(jwtCodec, userService);
            }

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다.")
            void it_returns_string(){
                assertThatThrownBy(
                        () -> authenticationService.parseToken(INVALID_TOKEN)
                ).isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("빈 토큰이 주어지면")
        class Context_with_a_empty_token{

            AuthenticationService authenticationService;
            UserService userService;

            @BeforeEach
            void setUp(){
                JwtCodec jwtCodec = new JwtCodec(SECRET);
                authenticationService = new AuthenticationService(jwtCodec, userService);
            }

            @ParameterizedTest(name = "[{index}] InvalidTokenException 예외를 던진다.")
            @NullAndEmptySource
            @ValueSource(strings = {"  ", "\t", "\n"})
            void it_returns_string(String emptyToken){
                assertThatThrownBy(
                            () -> authenticationService.parseToken(emptyToken)
                    ).isInstanceOf(InvalidTokenException.class);
            }
        }

    }
}


