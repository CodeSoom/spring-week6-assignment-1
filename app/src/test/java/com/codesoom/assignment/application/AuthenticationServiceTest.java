package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.PasswordMismatchException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_EMAIL = "test@naver.com";
    private static final String MATCH_PASSWORD = "12345678";
    private static final String INVALID_EMAIL = "desk@naver.com";
    private static final String NO_MATCH_PASSWORD = "5555";

    private AuthenticationService authenticationService;

    private UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp(){
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        authenticationService = new AuthenticationService(jwtUtil, userRepository);

        User user = User.builder()
                .id(1L)
                .email(VALID_EMAIL)
                .password(MATCH_PASSWORD).build();
        given(userRepository.findByEmailAndDeletedIsFalse(VALID_EMAIL)).willReturn(Optional.of(user));

        given(userRepository.findByEmailAndDeletedIsFalse(INVALID_EMAIL)).willReturn(Optional.empty());
    }
    @Test
    void loginWithMatchEmailAndPassword(){

        UserLoginData userLoginData = UserLoginData.builder()
                .email(VALID_EMAIL)
                .password(MATCH_PASSWORD)
                .build();

        String accessToken = authenticationService.login(userLoginData);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void loginWithMismatchPassword(){
        UserLoginData userLoginData = UserLoginData.builder()
                .email(VALID_EMAIL)
                .password(NO_MATCH_PASSWORD)
                .build();

        assertThatThrownBy(() -> authenticationService.login(userLoginData))
                .isInstanceOf(PasswordMismatchException.class);

    }

    @Test
    void loginWithInvalidEmailAndPassword(){

        UserLoginData userLoginData = UserLoginData.builder()
                .email(INVALID_EMAIL)
                .password(NO_MATCH_PASSWORD)
                .build();

        assertThatThrownBy(() -> authenticationService.login(userLoginData))
                .isInstanceOf(UserNotFoundException.class);
    }
    @Test
    void parseTokenWithValidToken(){
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken(){
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void parseTokenWithBlankToken(){
        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidTokenException.class);
    }

}