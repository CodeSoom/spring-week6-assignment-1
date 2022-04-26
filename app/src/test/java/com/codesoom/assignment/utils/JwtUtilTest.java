package com.codesoom.assignment.utils;

import com.codesoom.assignment.application.auth.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtUtilTest {

    private static final String SECRET_KEY = "NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3";
    private JwtUtil jwtUtil;

    private String VALID_TOKEN;
    private static final String INVALID_TOKEN
            = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ze4dJmmF4peSe1uo9-ug019VAwzhr0WO8H3iHroSO890";
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setup() {
        this.jwtUtil = new JwtUtil(SECRET_KEY);
        this.VALID_TOKEN = jwtUtil.encode(USER_ID);
    }

    @DisplayName("토큰을 성공적으로 생성한다.")
    @Test
    void encodeTest() {
        String token = jwtUtil.encode(USER_ID);
        assertThat(token).contains(".");
        assertThat(token.split("\\.").length).isEqualTo(3);
        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @DisplayName("유효한 토큰이 주어지면 검증에 성공한다.")
    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
    }

    @DisplayName("유효하지 않은 토큰이 주어지면 예외를 던진다.")
    @Test
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

}
