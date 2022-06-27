package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private final String SECRET = "12345678901234567890123456789012";
    private final long VALID_TIME = 24 * 60 * 60 * 1000;

    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsIm" +
            "lhdCI6MTY1NDg0MTM1NiwiZXhwIjoxNjU0OTI3NzU2fQ.qU12boCobw3ltksPjBcMXxE_0Lf7eSQUWJ0PwAn-z5I";

    private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsIml" +
            "hdCI6MTY1NDc1NzIxNSwiZXhwIjoxNjU0NzYwODE1fQ.NI_o2dC-go7A_IEJ34LOZPw5Ohg_KcVa48UUHDsKUpl";

    private final Long USER_ID = 1L;

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, VALID_TIME);
    }

    @DisplayName("encode 메서드에 userId가 주어지면 생성된 토큰을 리턴한다.")
    @Test
    void encode() {
        String token = jwtUtil.encode(USER_ID);

        assertThat(token).contains(".");
    }

    @DisplayName("decode 메서드에 유효한 토큰이 주어지면 클레임을 리턴한다.")
    @Test
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
    }

    @DisplayName("decode 메서드에 유효하지 않은 토큰이 주어지면 예외를 던진다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  ", INVALID_TOKEN})
    void decodeWithInvalidToken(String token) {
        assertThrows(InvalidTokenException.class, () -> jwtUtil.decode(token));
    }

    @DisplayName("calculationExpiration 메서드에 현재 시간과 유효 기간이 주어지면 만료시간을 리턴한다.")
    @Test
    void calculateExpiration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = jwtUtil.getClass()
                .getDeclaredMethod("calculationExpiration", Date.class, long.class);

        method.setAccessible(true);

        Date now = new Date();
        Date expiration = (Date) method.invoke(jwtUtil, now, VALID_TIME);

        assertThat(expiration).isAfter(now);
        assertThat(expiration.getTime()).isEqualTo(now.getTime() + VALID_TIME);
    }
}
