package com.codesoom.assignment.auth.infra;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTYxNDY2ODE2NiwiZXhwIjox" +
            "NjE0NjY4MTY2fQ.0DARRfpDRIQLjkJyX5K4UvJNO-0x9fAw3-6YPCA3tdA";
    private static final Long GIVEN_ID = 1L;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(SECRET);
    }

    @DisplayName("encode 메서드는 전달받은 식별값에 해당하는 토큰을 리턴한다")
    @Test
    void encode_id_to_token() {
        String token = jwtTokenProvider.encode(GIVEN_ID);

        assertThat(token).isNotEmpty();
    }

    @DisplayName("decode 메서드는 유효한 토큰에 담긴 id를 리턴한다")
    @Test
    void decodeWithValidToken() {
        Claims claims = jwtTokenProvider.decode(VALID_TOKEN);

        assertThat(claims.get("userId", Long.class)).isEqualTo(GIVEN_ID);
    }

}
