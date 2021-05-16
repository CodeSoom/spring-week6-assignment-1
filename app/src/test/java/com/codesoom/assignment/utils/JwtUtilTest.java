package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * JwtUtil 클래스
 */
class JwtUtilTest {

    private final String SECRET = "12345678901234567890123456789012";

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdakD";

    private JwtUtil jwtUtil;

    private final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    @DisplayName("Jwt encode()는 userId가 주어지면 생성된 토큰을 리턴한다")
    void encode() {

        String token = jwtUtil.encode(USER_ID);
        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("Jwt decode()는 유효한 토큰이 주어지면 Claim을 리턴한다")
    void decode_with_valid_token() {

        Claims claims = jwtUtil.decode(VALID_TOKEN);
        assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("Jwt decode()는 유효하지 않은 토큰이 주어지면 InvalidToken 예외를 던진다")
    void decode_with_invalid_token() {

        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode("  "))
                .isInstanceOf(InvalidTokenException.class);
    }
}
