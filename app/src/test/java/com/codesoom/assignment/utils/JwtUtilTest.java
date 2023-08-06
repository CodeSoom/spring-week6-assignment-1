package com.codesoom.assignment.utils;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("JwtUtil 클래스")
class JwtUtilTest {
    private final String SECRET = "12345678901234567890123456789010";
    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    JwtUtil jwtUtil = new JwtUtil(SECRET);
    private final Long USER_ID = 1L;

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class encode_메서드는 {
        @DisplayName("유저 아이디를 받아 토큰을 생성한다.")
        @Test
        void It_returns_token() {
            String token = jwtUtil.encode(USER_ID);

            assertThat(token).isNotNull();
            assertThat(token).isEqualTo(VALID_TOKEN);
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class decode_메서드는 {
        @DisplayName("토큰을 파싱하여 클레임을 반환한다.")
        @Test
        void It_returns_claims() {
            assertThat(jwtUtil.decode(VALID_TOKEN)).isNotNull();
            assertThat(jwtUtil.decode(VALID_TOKEN).get("userId",Long.class)).isEqualTo(USER_ID);
        }
    }
}
