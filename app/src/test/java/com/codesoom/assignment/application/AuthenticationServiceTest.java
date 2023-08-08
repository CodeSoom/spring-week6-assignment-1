package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest  {

    private static final String SECRET = "12345678901234567890123456789010";
    private AuthenticationService authenticationService;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final JwtUtil jwtUtil = new JwtUtil(SECRET);

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGKK";
    public static final String VALID_EMAIL = "dh@gmail.com";
    private static final String VALID_PASSWORD = "1111";
    public static final String INVALID_EMAIL = "dh";
    private static final String INVALID_PASSWORD = "0";
    private static final UserLoginData VALID_LOGIN_DATA = UserLoginData.builder()
                                                            .email(VALID_EMAIL)
                                                            .password(VALID_PASSWORD)
                                                            .build();
    private static final UserLoginData INVALID_LOGIN_DATA = UserLoginData.builder()
                                                            .email(INVALID_EMAIL)
                                                            .password(INVALID_PASSWORD)
                                                            .build();

    @BeforeEach
    void setup(){
        authenticationService = new AuthenticationService(jwtUtil,userRepository);
    }

    @Test
    void loginWithValidLoginData(){
        given(userRepository.findByEmail(VALID_EMAIL))
                .willReturn(Optional.of( User.builder()
                                            .id(1L)
                                            .email(VALID_EMAIL)
                                            .password(VALID_PASSWORD)
                                            .build()));

        String accessToken = authenticationService.login(VALID_LOGIN_DATA);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
        verify(userRepository).findByEmail(VALID_EMAIL);
    }

    @Test
    void loginWithInvalidLoginData(){
        given(userRepository.findByEmail(INVALID_EMAIL))
                .willThrow(new LoginFailException());

        assertThatThrownBy(() -> authenticationService.login(INVALID_LOGIN_DATA))
                .isInstanceOf(LoginFailException.class);
    }

    @Test
    void parseTokenWithValidToken() {
        Long id = authenticationService.parseToken(VALID_TOKEN);

        assertThat(id).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidAccessTokenException.class);
    }
}
