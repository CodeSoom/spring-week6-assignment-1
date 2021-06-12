package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private static final String secret = "1234567890123456789012234567890123456789012234567890123456789012234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    @BeforeEach
    public void setUp(){
        JwtUtil jwtUtil = new JwtUtil(secret);

        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    public void login(){
        String accessToken = authenticationService.login();

        System.out.println("accessToken = " + accessToken);

        assertThat(accessToken).contains(".");
    }

    @Test
    public void parseToken(){
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    public void parseTokeWithInvalidToken(){
        Long userId = authenticationService.parseToken(VALID_TOKEN);
        assertThatThrownBy(
                () -> authenticationService.parseToken(INVALID_TOKEN)
        ).isInstanceOf(InvalidAccessTokenException.class);
    }
}
