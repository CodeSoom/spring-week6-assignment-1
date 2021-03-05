package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.dto.LoginRequest;
import com.codesoom.assignment.auth.infra.JwtTokenProvider;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String GIVEN_USER_EMAIL = "test@test.com";
    private static final String GIVEN_USER_PASSWORD = "password";
    private static final String NOT_EXIST_EMAIL = GIVEN_USER_EMAIL + "_NOT_EXIST";
    private static final String WRONG_PASSWORD = GIVEN_USER_PASSWORD + "_WRONG";
    private static final Long GIVEN_ID = 1L;
    private static final long EXPIRED_TIME = 300000;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY0MDk2MjgwMCwiZXhwIjoxNj" +
            "QwOTYzMTAwfQ.DCmfit5LOuqhFHZoytRKluR9R-YVtxXLPgC-6PB3myAsjRAzlAgouWTA-GPkP_AudMK0CcySqhWpbR1BBCJCVw";
    private AuthenticationService authenticationService;

    @Value("${jwt.secret}")
    private String secret;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secret, EXPIRED_TIME);
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
            final String email = GIVEN_USER_EMAIL;
            final String password = GIVEN_USER_PASSWORD;
            final LoginRequest requestDto = new LoginRequest(email, password);

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(GIVEN_USER_EMAIL))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("인증 토큰을 리턴한다.")
            @Test
            void it_returns_token() {
                String token = authenticationService.authenticate(requestDto);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 이메일이 주어지면")
        class Context_with_not_exist_email {
            final String email = NOT_EXIST_EMAIL;
            final String password = GIVEN_USER_PASSWORD;
            final LoginRequest requestDto = new LoginRequest(email, password);

            @DisplayName("예외를 던진다.")
            @Test
            void It_throws_exception() {
                assertThrows(IllegalArgumentException.class,
                        () -> authenticationService.authenticate(requestDto));
            }
        }

        @Nested
        @DisplayName("유효하지않는 비밀번호가 주어지면")
        class Context_with_wrong_password {
            final String email = GIVEN_USER_EMAIL;
            final String password = WRONG_PASSWORD;
            final LoginRequest requestDto = new LoginRequest(email, password);

            @BeforeEach
            void setUp() {
                given(userRepository.findByEmail(GIVEN_USER_EMAIL))
                        .willReturn(Optional.of(user));
            }

            @DisplayName("예외를 던진다.")
            @Test
            void It_throws_exception() {
                assertThrows(IllegalArgumentException.class,
                        () -> authenticationService.authenticate(requestDto));
            }
        }
    }

    @Nested
    @DisplayName("parseToken 메서드는")
    class Describe_parseToken {
        @Nested
        @DisplayName("토큰이 주어지면")
        class Context_with_token {
            final String token = VALID_TOKEN;

            @DisplayName("토큰에 담긴 정보를 리턴한다.")
            @Test
            void it_returns_user_id() {
                final Claims actual = authenticationService.parseToken(token);

                assertThat(actual.get("userId", Long.class)).isEqualTo(GIVEN_ID);
                assertThat(actual.getIssuedAt()).isNotNull();
                assertThat(actual.getExpiration()).isNotNull();
            }
        }
    }
}
