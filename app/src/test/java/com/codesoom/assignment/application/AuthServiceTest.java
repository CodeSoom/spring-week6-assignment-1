package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class AuthServiceTest {
    private AuthService authService;
    private JwtUtil jwtUtil;

    private User userFixture;

    private final String KEY = "12345678901234567890123456789012";
    private final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";

    @BeforeEach
    void setupTest() {
        jwtUtil = new JwtUtil(KEY);
        authService = new AuthService(jwtUtil);
    }

    @BeforeEach
    void setupFixtures(){
        userFixture = User.builder()
            .id(1L)
            .name("name")
            .password("password")
            .email("email@email.com")
            .build();
    }

    @DisplayName("getToken")
    @Nested
    class GetTokenTest {
        @DisplayName("returns a token")
        @Test
        void returnsToken() {
            String token = authService.getToken(userFixture);

            assertThat(token.matches(JWT_REGEX)).isTrue();
        }
    }

    @DisplayName("parseToken")
    @Nested
    class ParseTokenTest {
        String token;

        @BeforeEach
        void setupToken() {
            token = authService.getToken(userFixture);
        }

        @DisplayName("returns an user id")
        @Test
        void returnsUserId() {
            Long id = authService.parseToken(token);

            assertThat(id).isEqualTo(userFixture.getId());
        }
    }
}
