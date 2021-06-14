package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.TokenManagerRepository;
import com.codesoom.assignment.domain.TokenManager;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@DisplayName("AuthenticationService")
class AuthenticationServiceTest {

    private final static String SECRETE_KEY = "12345678901234567890123456789010";
    private final static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".cjDHNEbvUC6G7AORn068kENYHYnOTIaMsgjD0Yyygn4";
    private final static String INVALID_TOKEN = VALID_TOKEN + "WRONG";
    private final static String BEARER = "BEARER ";
    private final static String VALID_ACCESS_TOKEN = BEARER + VALID_TOKEN;
    private final static String INVALID_ACCESS_TOKEN = BEARER + INVALID_TOKEN;
    private final Long expiredDate = 30000L;
    private Mapper mapper;

    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;
    private UserRepository userRepository = mock(UserRepository.class);
    private TokenManagerRepository tokenManagerRepository = mock(TokenManagerRepository.class);

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRETE_KEY, expiredDate);
        mapper = DozerBeanMapperBuilder.buildDefault();
        authenticationService = new AuthenticationService(mapper, jwtUtil, userRepository, tokenManagerRepository);
    }

    @Nested
    @DisplayName("login메서드는")
    class Describe_Login {
        @Nested
        @DisplayName("유저 정보를 전달 받으면")
        class Context_Valid_Login {
            private Long id = 1L;
            private String email = "test@example.com";
            private String password = "test";
            private SessionRequestData sessionRequestData;

            @BeforeEach
            void setUpValidLogin() {
                sessionRequestData = SessionRequestData.builder()
                        .email(email)
                        .password(password)
                        .build();
                User user = User.builder()
                        .id(id)
                        .email(email)
                        .password(password)
                        .build();
                given(userRepository.findByEmail(email))
                        .willReturn(Optional.of(user));
            }

            @Test
            @DisplayName("토큰을 발급한다.")
            void valid_login_create_token() {
                String accessToken = authenticationService.login(sessionRequestData);
                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_ParseToken {
        @Nested
        @DisplayName("정상적인 토큰을 받으면")
        class Context_Valid_ParseToken {
            private Long userId = 1L;

            @Test
            @DisplayName("사용자 아이디를 반환한다.")
            void valid_parseToken_return_userId() {
                Long parsingUserId = authenticationService.parseToken(VALID_TOKEN);
                assertThat(parsingUserId).isEqualTo(userId);
            }
        }

        @Nested
        @DisplayName("비정상적인 토큰을 받으면")
        class Context_Invalid_ParseToken {
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            void invalid_parsetToken_exception() {
                assertThatThrownBy(() -> {
                    authenticationService.parseToken(INVALID_TOKEN);
                }).isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("비어있는 토큰을 받으면")
        class Context_Empty_ParseToken {
            @ParameterizedTest(name = "{index} [{arguments}] InvalidException을 던진다. ")
            @ValueSource(strings = {" "})
            @NullAndEmptySource
            void empty_parseToken_exeption(String emptyToken) {
                assertThatThrownBy(() -> authenticationService.parseToken(emptyToken))
                        .isInstanceOf(InvalidTokenException.class);

            }
        }
    }

    @Nested
    @DisplayName("accessTokenCheck 메서드는")
    class Describe_AccessTokenCheck {
        @Nested
        @DisplayName("정상적인 토큰 값을 보내면")
        class Context_Valid_AccessTokenCheck {
            @Test
            @DisplayName("별도의 Exception을 던지지 않는다.")
            void valid_accessTokenCheck() {
                assertTrue(authenticationService.accessTokenCheck(VALID_ACCESS_TOKEN));
            }
        }

        @Nested
        @DisplayName("잘못된 토큰 값을 보내면")
        class Context_Invalid_AccessTokenCheck {
            @Test
            @DisplayName("InvalidTokenException을 던진다.")
            void invalid_accessTokenCheck_exception() {
                assertThatThrownBy(() -> {
                    authenticationService.accessTokenCheck(INVALID_ACCESS_TOKEN);
                }).isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("비어있는 토큰 값을 보내면")
        class Context_Empty_AccessTokenCheck {
            @ParameterizedTest(name = "{index} [{arguments}] InvalidTokenException을 던진다.")
            @NullAndEmptySource
            @ValueSource(strings = {" "})
            void invalid_accessTokenCheck_exception(String emptyToken) {
                assertThatThrownBy(() -> {
                    authenticationService.accessTokenCheck(emptyToken);
                }).isInstanceOf(InvalidTokenException.class);
            }
        }
    }

    @Nested
    @DisplayName("saveToken 메서드는")
    class Describe_SaveToken {
        private TokenManager tokenManager;

        @BeforeEach
        void setUpValidSaveToken() {
            tokenManager = TokenManager.builder()
                    .token(VALID_TOKEN)
                    .createTokenDate(LocalDateTime.now())
                    .build();
            given(tokenManagerRepository.save(tokenManager))
                    .willReturn(tokenManager);
        }

        @DisplayName("토큰 정보를 저장후 사용자 토큰 정보를 반환한다.")
        @Test
        void valid_save_token() {
            TokenManager savedTokenManager = tokenManagerRepository.save(tokenManager);
            assertThat(savedTokenManager.getToken())
                    .isEqualTo(VALID_TOKEN);
        }
    }
}
