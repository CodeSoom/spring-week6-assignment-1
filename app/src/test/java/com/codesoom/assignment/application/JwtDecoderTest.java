package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JwtDecoderTest {

    private static final String SECRET = "12345678901234567890123456789012";

    private long id;

    private String token;
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        JwtEncoder jwtEncoder = new JwtEncoder(SECRET);

        id = 1L;
        User user = User.builder()
            .id(id)
            .email("sprnd645@gmail.com")
            .password("password")
            .build();

        token = jwtEncoder.encode(user)
            .getAccessToken();

        jwtDecoder = new JwtDecoder(SECRET);
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {


        @Nested
        @DisplayName("올바른 토큰이 주어진 경우")
        class Context_validToken {

            private String validToken;

            @BeforeEach
            void setUp() {
                validToken = token;
            }

            @Test
            @DisplayName("클레임을 리턴한다")
            void it_returns_claims() throws SignatureException {
                Claims claims = jwtDecoder.decode(validToken);

                assertThat(claims.get("userId", Long.class))
                    .isEqualTo(id);
            }
        }

        @Nested
        @DisplayName("올바르지 않은 토큰이 주어진 경우")
        class Context_invalidToken {

            private String invalidToken;

            @BeforeEach
            void setUp() {
                invalidToken = token.substring(0, token.length() - 1);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws() {
                assertThatThrownBy(() -> jwtDecoder.decode(invalidToken))
                    .isInstanceOf(SignatureException.class);
            }
        }
    }
}
