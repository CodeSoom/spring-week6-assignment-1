package com.codesoom.assignment.application;

import com.codesoom.assignment.AuthenticationTestFixture;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.UserAuthenticationFailedException;
import com.codesoom.assignment.errors.UserEmailNotExistException;
import com.codesoom.assignment.utils.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private AuthenticationService authService;
    private final User GENERAL_USER = AuthenticationTestFixture.GENERAL_USER;
    private final User NON_EXISTENT_USER = AuthenticationTestFixture.NON_EXISTENT_USER;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        authService = new AuthenticationService(
                new JwtUtil(AuthenticationTestFixture.SECRET_KEY),
                userRepository
        );

        given(userRepository.findByEmail(GENERAL_USER.getEmail()))
                .willReturn(Optional.of(GENERAL_USER));
    }

    @Nested
    @DisplayName("encode")
    class Describe_encode {
        @Nested
        @DisplayName("user id가 주어졌을 때")
        class Context_user_id {
            Long givenUserId = GENERAL_USER.getId();

            @DisplayName("인코딩된 token을 반환한다.")
            @Test
            void it_returns_encoded_token() {
                String code = authService.encode(givenUserId);
                assertThat(code).isEqualTo(AuthenticationTestFixture.VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode")
    class Describe_decode {
        @Nested
        @DisplayName("token이 주어졌을 때")
        class Context_with_token {
            String givenToken = AuthenticationTestFixture.VALID_TOKEN;

            @DisplayName("user id를 반환한다.")
            @Test
            void it_returns_user_id() {
                Long userId = authService.decode(givenToken);
                assertThat(userId).isEqualTo(GENERAL_USER.getId());
            }
        }
    }

    @Nested
    @DisplayName("createSession")
    class Describe_create_session {
        @Nested
        @DisplayName("등록된 유저의 email, 일치하는 password가 주어진다면")
        class Context_with_valid_email_password {
            String givenEmail = GENERAL_USER.getEmail();
            String givenPassword = GENERAL_USER.getPassword();

            @DisplayName("token을 반환한다")
            @Test
            void it_returns_token() {
                String token = authService.createSession(givenEmail, givenPassword);
                assertThat(token).isEqualTo(AuthenticationTestFixture.VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 유저의 email이 주어진다면")
        class Context_with_not_exist_email {
            String givenEmail = NON_EXISTENT_USER.getEmail();
            String givenPassword = GENERAL_USER.getPassword();

            @DisplayName("user email이 존재하지 않는다는 예외를 던진다")
            @Test
            void it_returns_user_email_not_exist_exception() {
                assertThrows(UserEmailNotExistException.class,
                        () -> authService.createSession(givenEmail, givenPassword));
            }
        }

        @Nested
        @DisplayName("등록된 유저의 email, 일치하지 않는 password가 주어진다면")
        class Context_with_invalid_password {
            String givenEmail = GENERAL_USER.getEmail();
            String givenPassword = NON_EXISTENT_USER.getPassword();

            @DisplayName("user 인증에 실패했다는 예외를 던진다")
            @Test
            void it_returns_user_auth_failed_exception() {
                assertThrows(UserAuthenticationFailedException.class,
                        () -> authService.createSession(givenEmail, givenPassword));
            }
        }
    }
}
