package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJ1c2VySWQiOjF9.bOQfc4mYhcmbCH6JjPuzSc6eRJEr_A0d8tVrPtxE9aU";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJ1c2VySWQiOjF9.bOQfc4mYhcmbCH6JjPuzSc6eRJEr_A0d8tVrPtxE9a0";

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        authenticationService = new AuthenticationService(jwtUtil);
    }

    @Test
    @DisplayName("login")
    public void login() throws Exception {
        String accessToken = authenticationService.login(1L);
        assertThat(accessToken).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("parseTokenWithValidToken")
    public void parseTokenWithValidToken() throws Exception{
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("parseTokenWithInvalidToken")
    public void parseTokenWithInvalidToken() throws Exception{
        assertThatThrownBy(()->authenticationService.parseToken(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("parseTokenWithBlankToken")
    public void parseTokenWithBlankToken() throws Exception{
        assertThatThrownBy(()->authenticationService.parseToken(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(()->authenticationService.parseToken(null))
                .isInstanceOf(InvalidTokenException.class);
    }

}