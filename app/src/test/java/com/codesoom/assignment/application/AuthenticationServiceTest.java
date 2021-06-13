package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("Describe: 인증 서비스 테스트")
class AuthenticationServiceTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private static final String SECRET = "12345678901234567890123456789012";

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(
                jwtUtil, userRepository);

        User user = User.builder()
                .password("test").build();

        given(userRepository.findByEmail("code@soom.com")).willReturn(Optional.of(user));
    }

    @Test
    @DisplayName("Describe: 올바른 이메일과 비밀번호로 로그인합니다.")
    void loginWithRightEmailPassword() {
        String accessToken = authenticationService.login("code@soom.com", "test");
        assertThat(accessToken).isEqualTo(VALID_TOKEN);

        verify(userRepository).findByEmail("code@soom.com");
    }

    @Test
    @DisplayName("Describe: 올바르지 않은 이메일로 로그인합니다.")
    void loginWithWrongEmail() {
        assertThatThrownBy(
                () -> authenticationService.login("wrongEmail", "test"))
                .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("wrongEmail");
    }

    @Test
    @DisplayName("Describe: 올바르지 않은 비밀번호로 로그인합니다.")
    void loginWithWrongPassword() {
        assertThatThrownBy(
                () -> authenticationService.login("code@soom.com", "wrongPassword"))
                .isInstanceOf(LoginFailException.class);

        verify(userRepository).findByEmail("code@soom.com");
    }

    @Test
    @DisplayName("Describe: 유효한 토큰일 때 사용자의 아이디를 검증합니다.")
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("Describe: 유효하지 않은 토큰일 때 예외처리를 발생시킵니다.")
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(
                () -> authenticationService.parseToken(INVALID_TOKEN)
        ).isInstanceOf(InvalidTokenException.class);
    }
}
