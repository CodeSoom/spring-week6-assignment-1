package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil 의")
class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final String USER_1_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private final JwtUtil jwtUtil = new JwtUtil(SECRET);

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode_method {
        @Nested
        @DisplayName("userId 를 받으면")
        class Context_with_userId {
            private final Long userId = 1L;

            @Test
            @DisplayName("payload 가 userId 인 토큰 문자열을 반환한다.")
            void it_returns_token() {
                String token = jwtUtil.encode(userId);
                assertThat(token).isEqualTo(USER_1_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode_method {
        @Nested
        @DisplayName("payload 가 {userId:1} 인 JWT 문자열을 받으면")
        class Context_with_json_web_token {
            @Test
            @DisplayName("{userId:1} 를 가진 Claims 를 반환한다.")
            void it_returns_claims() {
                Claims claims = jwtUtil.decode(USER_1_TOKEN);
                Long userId = claims.get("userId", Long.class);
                assertThat(userId).isEqualTo(1L);
            }
        }
    }
}