package com.codesoom.assignment.utils;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("JwtUtil 클래스")
class JwtUtilTest {
    private final String SECRET = "12345678901234567890123456789010";
    private final String VALID_TOKEN = "";
    JwtUtil jwtUtil = new JwtUtil(SECRET);

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class Describe_encode {
        @DisplayName("유저 아이디를 받아 토큰을 생성한다.")
        @Test
        void It_returns_token() {
            String token = jwtUtil.encode(1L);

            Assertions.assertThat(token).isNotNull();
            Assertions.assertThat(token).isEqualTo(VALID_TOKEN);
        }
    }
}
