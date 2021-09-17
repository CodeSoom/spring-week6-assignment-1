package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class AuthenticationServiceTest {

    private final AuthenticationService authenticationService = mock(AuthenticationService.class);

    private static final String SECRET = "12345678901234567890123456789010";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final String INVALID_TOKEN = VALID_TOKEN + "wrong";

    private static final String EXISTED_EMAIL = "test@gmail.com";

    @BeforeEach
    void setUp() {
        given(authenticationService.login(any(LoginRequestData.class))).willReturn(VALID_TOKEN);

        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

        given(authenticationService.parseToken(INVALID_TOKEN)).willThrow(new InvalidAccessTokenException(INVALID_TOKEN));

        given(authenticationService.parseToken("")).willThrow(new InvalidAccessTokenException(""));

        given(authenticationService.parseToken(null)).willThrow(new InvalidAccessTokenException(null));
    }

    @Test
    void login() {
        LoginRequestData loginRequestData = LoginRequestData.builder()
                .email(EXISTED_EMAIL)
                .password("1234")
                .build();

        String accessToken = authenticationService.login(loginRequestData);
        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInValidToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidAccessTokenException.class);
    }

    @Test
    void parseTokenWithBlankToken() {
        assertThatThrownBy(() -> authenticationService.parseToken(""))
                .isInstanceOf(InvalidAccessTokenException.class);

        assertThatThrownBy(() -> authenticationService.parseToken(null))
                .isInstanceOf(InvalidAccessTokenException.class);

        Stream.of("", null).forEach((it) -> {
            assertThatThrownBy(() -> authenticationService.parseToken(it))
                    .isInstanceOf(InvalidAccessTokenException.class);
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    void parseTokenWithBlankToken(String token) {
        assertThatThrownBy(() -> authenticationService.parseToken(token))
                .isInstanceOf(InvalidAccessTokenException.class);
    }
}
