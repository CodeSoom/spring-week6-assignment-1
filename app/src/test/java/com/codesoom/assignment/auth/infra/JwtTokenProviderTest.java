package com.codesoom.assignment.auth.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9." +
            "neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGG0";
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

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

}
