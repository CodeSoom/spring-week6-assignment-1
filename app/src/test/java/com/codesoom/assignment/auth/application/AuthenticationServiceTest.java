package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.infra.JwtTokenProvider;
import com.codesoom.assignment.user.application.UserEmailNotFoundException;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String GIVEN_USER_EMAIL = "test@test.com";
    private static final String GIVEN_USER_PASSWORD = "password";
    private static final String NOT_EXIST_EMAIL = "not_exist@test.com";
    private static final String WRONG_PASSWORD = "wrong_password";
    private static final Long GIVEN_ID = 1L;
    private static final long EXPIRED_TIME = 300000;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY0MDk2MjgwMCwiZXh" +
            "wIjoxNjQwOTYzMTAwfQ.2siRnBJmRU2JXjZY0CkQMgnCHRJN4Dld4_wG6R7T-HQ";

    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET, EXPIRED_TIME);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "time", LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0));
        authenticationService = new AuthenticationService(jwtTokenProvider, userRepository);

        user = User.builder()
                .id(GIVEN_ID)
                .password(GIVEN_USER_PASSWORD)
                .email(GIVEN_USER_EMAIL)
                .build();
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    class Describe_authenticate {

        @Nested
        @DisplayName("등록된 이메일과 비밀번호가 주어지면")
        class Context_with_exist_email_password {
            String email;
            String password;

            @BeforeEach
            void setUp() {
                email = GIVEN_USER_EMAIL;
                password = GIVEN_USER_PASSWORD;

                given(userRepository.findByEmail(GIVEN_USER_EMAIL))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("인증 토큰을 리턴한다.")
            @Test
            void it_returns_token() {
                String token = authenticationService.authenticate(email, password);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 이메일이 주어지면")
        class Context_with_not_exist_email {
            String email;
            String password;

            @BeforeEach
            void setUp() {
                email = NOT_EXIST_EMAIL;
                password = GIVEN_USER_PASSWORD;

                given(userRepository.findByEmail(NOT_EXIST_EMAIL))
                        .willThrow(new UserEmailNotFoundException(NOT_EXIST_EMAIL));
            }

            @DisplayName("예외를 발생시킨다.")
            @Test
            void It_throws_exception() {
                assertThrows(UserEmailNotFoundException.class,
                        () -> authenticationService.authenticate(email, password));
            }
        }

        @Nested
        @DisplayName("유효하지않는 비밀번호가 주어지면")
        class Context_with_wrong_password {
            String email;
            String password;

            @BeforeEach
            void setUp() {
                email = GIVEN_USER_EMAIL;
                password = WRONG_PASSWORD;

                given(userRepository.findByEmail(GIVEN_USER_EMAIL))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("예외를 발생시킨다.")
            @Test
            void It_throws_exception() {
                assertThrows(IllegalArgumentException.class,
                        () -> authenticationService.authenticate(email, password));
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {
        @Nested
        @DisplayName("토큰이 주어지면")
        class Context_with_token {
            String token = VALID_TOKEN;

            @DisplayName("토큰에 담긴 정보를 리턴한다.")
            @Test
            void it_returns_user_id() {
                Claims actual = authenticationService.parseToken(token);

                assertThat(actual.get("userId", Long.class)).isEqualTo(GIVEN_ID);
                assertThat(actual.getIssuedAt()).isNotNull();
                assertThat(actual.getExpiration()).isNotNull();
            }
        }
    }
}
