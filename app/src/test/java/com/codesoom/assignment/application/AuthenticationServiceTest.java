package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("AuthenticationService")
class AuthenticationServiceTest {

    private final static String  SECRETE_KEY = "12345678901234567890123456789010";
    private final static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".cjDHNEbvUC6G7AORn068kENYHYnOTIaMsgjD0Yyygn4";
    private final static String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".cjDHNEbvUC6G7AORn068kENYHYnOTIaMsgjD0Yyyg11";

    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil(SECRETE_KEY);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Nested
    @DisplayName("login메서드는")
    class Describe_Login{
        @Nested
        @DisplayName("유저 아이디를 전달 받으면")
        class Context_Valid_Login{
            @Test
            @DisplayName("토큰을 발급한다.")
            void valid_login_create_token(){
                String accessToken = authenticationService.login();
                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_ParseToken{
        @Nested
        @DisplayName("정상적인 토큰을 받으면")
        class Context_Valid_ParseToken{
            private Long userId = 1L;
            @Test
            @DisplayName("사용자 아이디를 반환한다.")
            void valid_parseToken_return_userId(){
                Long parsingUserId = authenticationService.parseToken(VALID_TOKEN);
                assertThat(parsingUserId).isEqualTo(userId);
            }
        }
        @Nested
        @DisplayName("비정상적인 토큰을 받으면")
        class Context_Invalid_ParseToken{
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            void invalid_parsetToken_exception(){
                assertThatThrownBy(() -> {
                    authenticationService.parseToken(INVALID_TOKEN);
                }).isInstanceOf(InvalidTokenException.class);
            }
        }
        @Nested
        @DisplayName("비어있는 토큰을 받으면")
        class Context_Empty_ParseToken{
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            void empty_parseToken_exeption(){
                assertAll(
                        ()->{
                            assertThatThrownBy(() -> authenticationService.parseToken(" "))
                                    .isInstanceOf(InvalidTokenException.class);
                        },
                        ()->{
                            assertThatThrownBy(() -> authenticationService.parseToken("  "))
                                    .isInstanceOf(InvalidTokenException.class);
                        },
                        ()->{
                            assertThatThrownBy(() -> authenticationService.parseToken(null))
                                    .isInstanceOf(InvalidTokenException.class);
                        }
                );
            }
        }
    }
}
