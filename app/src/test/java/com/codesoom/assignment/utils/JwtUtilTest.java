package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Nested
@DisplayName("JwtUtil 클래스의")
class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789010";
    private static final Long VALID_USER_ID = 1L;
    private static final String VALID_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_HEADER_TOKEN =
            "eyJhbGciOiJIUzI1Ni12.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_SIGNATURE_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGas";
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메소드는")
    class DescribeEncode {

        @Nested
        @DisplayName("유저 식별자를 받으면")
        class ContextWithUserId {

            @Test
            @DisplayName("토큰을 반환합니다")
            void ItReturnsToken() {
                assertThat(jwtUtil.encode(VALID_USER_ID))
                        .isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Test
    void decode() {
//        Claims claims = jwtUtil.decode(TOKEN);
//
//        assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
    }

    @Nested
    @DisplayName("decode 메소드는")
    class DescribeDecode {

        @Nested
        @DisplayName("올바른 JWT를 받으면")
        class ContextWithValidToken {

            @Test
            @DisplayName("해독된 정보를 반환합니다")
            void ItReturnsDecoded() {
                assertThat(jwtUtil.decode(VALID_TOKEN).getUserId())
                        .isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("Header가 올바르지 않은 토큰을 받으면")
        class ContextWithInvalidHeaderToken {

            @Test
            @DisplayName("올바르지 않은 형식의 JWT에 대한 예외를 던집니다")
            void ItThrowsMalformedJwtException() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_HEADER_TOKEN))
                        .isInstanceOf(MalformedJwtException.class);
            }
        }

        @Nested
        @DisplayName("Signature가 올바르지 않은 토큰을 받으면")
        class ContextWithInvalidSignatureToken {

            @Test
            @DisplayName("틀린 Signature에 대한 예외를 던집니다")
            void ItThrowsSignatureException() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_SIGNATURE_TOKEN))
                        .isInstanceOf(SignatureException.class);
            }
        }
    }
}
