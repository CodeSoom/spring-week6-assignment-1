package com.codesoom.assignment.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtUtil 테스트")
class JwtUtilTest {
    private JwtUtil jwtUtil;

    private static final String SECRET = "12345678901234567890123456789010";
    private static final String EXISTED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGlzdGVkRW1haWwifQ." +
            "UQodS3elf3Cu4g0PDFHqVloFbcKHHmTTnk0jGmiwPXY";

    private static final String EXISTED_EMAIL = "existedEmail";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {
        @Nested
        @DisplayName("만약 저장되어 있는 이메일이 주어진다면")
        class Context_WithExistedEmail {
            private final String givenExistedEmail = EXISTED_EMAIL;

            @Test
            @DisplayName("주어진 이메일로 토큰을 생성하고 리턴한다")
            void itCreatesTokenAndReturnsToken () {
                String token = jwtUtil.encode(givenExistedEmail);

                assertThat(token).isEqualTo(EXISTED_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {
        @Nested
        @DisplayName("만약 유효한 토큰이 주어진다면")
        class Context_WithExistedToken {
            private final String givenExistedToken = EXISTED_TOKEN;

            @Test
            @DisplayName("주어진 토큰을 해석하여 내용을 리턴한다")
            void itReturnsClaims() {
                Claims claims = jwtUtil.decode(givenExistedToken);

                assertThat(claims.getSubject()).isEqualTo(EXISTED_EMAIL);
            }
        }
    }
}
