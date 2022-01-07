package com.codesoom.assignment.utills;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtill 클래스")
class JwtUtillTest {
    private static final String SECRET = "12345678901234567890123456789010";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private static final String INVALID_TOKEN = VALID_TOKEN + "000";

    private JwtUtill jwtUtill;

    @BeforeEach
    void setUp() {
        jwtUtill = new JwtUtill(SECRET);
    }

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {
        @Nested
        @DisplayName("사용자 정보가 들어오면")
        class Context_with_secret {
            @Test
            @DisplayName("암호화하여 리턴한다")
            void it_return_string() {
                String token = jwtUtill.encode(1L);

                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("dencode 메소드는")
    class Describe_decode {
        @Nested
        @DisplayName("인증된 토큰이 들어오면")
        class Context_with_valid_Token {
            @Test
            @DisplayName("디코딩된 사용자 정보를 리턴한다")
            void it_return_string() {
                Claims claims = jwtUtill.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("인증되지 않은 토큰이 들어오면")
        class Context_with_Invalid_Token {
            @Test
            @DisplayName("SignatureException 예외를 던진다.")
            void it_return_string() {
                assertThatThrownBy(() -> jwtUtill.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("빈 토큰이 들어오면")
        class Context_with_Empty_Token {
            String[] emptyToken = new String[]{null, "", "       "};

            @Test
            @DisplayName("SignatureException 예외를 던진다.")
            void it_return_string() {
                for (String token : emptyToken) {
                    assertThatThrownBy(() -> jwtUtill.decode(token))
                            .isInstanceOf(InvalidTokenException.class);
                }
            }
        }
    }
}
