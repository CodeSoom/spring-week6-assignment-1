package com.codesoom.assignment.utils;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.exception.DecodingInValidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {
    private static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJNRU1CRVIifQ.X99CPxQFtgjARrg9bqR6bQkp6JFMN9a-XUo9GAdO4so";
    private static String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJNRU1CRVIifQ.X99CPxQFtgjARrg9bqR6bQkp6JFMN9a-XUo9GAdO4so111";
    private static String SECRET = "12345678901234567890123456789010";
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encodeToken() {
        assertThat(jwtUtil.encode(1L, Role.MEMBER)).isEqualTo("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvbGUiOiJNRU1CRVIifQ.X99CPxQFtgjARrg9bqR6bQkp6JFMN9a-XUo9GAdO4so");
    }

    @Test
    void parseUserIdWithValidToken() {
        assertThat(jwtUtil.decode(VALID_TOKEN).get("userId", Long.class)).isEqualTo(1L);
    }

    @Test
    void parseRoleWithValidToken() {
        assertThat(jwtUtil.decode(VALID_TOKEN).get("role", String.class)).isEqualTo("MEMBER");
    }

    @Test
    void decodeWithInValidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(DecodingInValidTokenException.class);
    }
}
