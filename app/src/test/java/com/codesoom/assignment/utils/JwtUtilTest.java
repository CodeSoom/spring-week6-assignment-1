package com.codesoom.assignment.utils;

import com.codesoom.assignment.application.auth.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtUtilTest {

    private static final String SECRET_KEY = "NcRfUjXn2r5u8x/A?D(G+KbPdSgVkYp3";
    private JwtUtil jwtUtil;

    private static final Long USER_ID = 1L;
    private String VALID_TOKEN;

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

    @DisplayName("decode 메서드는")
    @Nested
    class Describe_decode {

        @DisplayName("유효한 토큰이 주어지면")
        @Nested
        class Context_with_valid_token {

            private String VALID_TOKEN;

            @BeforeEach
            void setup() {
                this.VALID_TOKEN = jwtUtil.encode(USER_ID);
            }

            @DisplayName("검증에 성공하며 claims를 반환한다.")
            @Test
            void it_returns_claims() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
            }
        }

        @DisplayName("유효하지 않은 토큰이 주어지면")
        @Nested
        class Context_with_invalid_token {

            @DisplayName("예외를 던진다.")
            @ParameterizedTest(name = "[{index}] {arguments} => {displayName}")
            @NullSource
            @ValueSource(strings
                    = { "", " "
                    ,"eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ze4dJmmF4peSe1uo9-ug019VAwzhr0WO8H3iHroSO890"
                    , "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9"
                    , "eyJhbGciOiJIUzI1NiJ9"
                    , "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEf.3290392!@#"})
            void it_throws_invalid_token_exception(String invalidToken) {
                assertThatThrownBy(() -> jwtUtil.decode(invalidToken))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }





}
