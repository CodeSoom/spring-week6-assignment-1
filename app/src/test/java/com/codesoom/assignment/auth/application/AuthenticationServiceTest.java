package com.codesoom.assignment.auth.application;

import com.codesoom.assignment.auth.infra.JwtTokenProvider;
import com.codesoom.assignment.user.domain.User;
import com.codesoom.assignment.user.domain.UserRepository;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String USER_EMAIL = "test@test.com";
    private static final String USER_PASSWORD = "password";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY0MDk2MjgwMCwiZXh" +
            "wIjoxNjQwOTYzMTAwfQ.2siRnBJmRU2JXjZY0CkQMgnCHRJN4Dld4_wG6R7T-HQ";
    private static final Long GIVEN_ID = 1L;
    private static final long EXPIRED_TIME = 300000;

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
                .email(USER_EMAIL)
                .build();

        given(userRepository.findByEmail(USER_EMAIL))
                .willReturn(Optional.of(user));
    }

    @Nested
    @DisplayName("authenticate 메서드는")
    class Describe_authenticate {
        @Nested
        @DisplayName("사용자 이메일과 비밀번호가 주어지면")
        class Context_with_user_id_password {
            String email = USER_EMAIL;
            String password = USER_PASSWORD;

            @DisplayName("인증 토큰을 리턴한다.")
            @Test
            void it_returns_token() {
                String token = authenticationService.authenticate(email, password);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

}
