package com.codesoom.assignment.auth.infra;

import com.codesoom.assignment.auth.application.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenProviderTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final Long GIVEN_ID = 1L;
    private static final long EXPIRED_TIME = 300_000;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTY0MDk2MjgwMCwiZXh" +
            "wIjoxNjQwOTYzMTAwfQ.2siRnBJmRU2JXjZY0CkQMgnCHRJN4Dld4_wG6R7T-HQ";
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(SECRET, EXPIRED_TIME);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "time", LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0));
    }

    @Nested
    @DisplayName("encode 메서드는 ")
    class Describe_encode {

        @Nested
        @DisplayName("사용자 id가 주어지면")
        class Context_with_user_id {
            Long userid;

            @BeforeEach
            void setUp() {
                userid = GIVEN_ID;
            }

            @DisplayName("사용자 토큰을 리턴한다.")
            @Test
            void It_returns_token() {
                String token = jwtTokenProvider.createToken(userid);

                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는 ")
    class Describe_decode {

        @Nested
        @DisplayName("토큰이 주어지면")
        class Context_with_token {
            String token;

            @BeforeEach
            void setUp() {
                token = VALID_TOKEN;
            }

            @DisplayName("토큰을 해독해 사용자 정보를 리턴한다.")
            @Test
            void It_returns_claim() {
                Claims claims = jwtTokenProvider.decode(token);

                assertThat(claims.get("userId", Long.class)).isEqualTo(GIVEN_ID);
                assertThat(claims.getIssuedAt()).isNotNull();
                assertThat(claims.getExpiration()).isNotNull();
            }
        }

        @Nested
        @DisplayName("비어있는 토큰이 주어지면")
        class Context_with_empty_token {

            @DisplayName("예외를 발생시킨다.")
            @ParameterizedTest
            @NullAndEmptySource
            void It_throws_exception(String token) {
                assertThrows(InvalidTokenException.class,
                        () -> jwtTokenProvider.decode(token));
            }
        }
    }

}
