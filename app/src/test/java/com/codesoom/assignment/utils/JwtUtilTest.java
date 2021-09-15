package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JwtUtilTest {
    private JwtUtil jwtUtil;

    private final String KEY = "12345678901234567890123456789012";
    private final String JWT_REGEX = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil(KEY);
    }

    @DisplayName("encode")
    @Nested
    class Encode {
        @DisplayName("returns a signed token")
        @Test
        void returnSignedToken() {
            String token = jwtUtil.encode(1L);

            assertThat(token.matches(JWT_REGEX)).isTrue();
        }
    }

    @DisplayName("decode")
    @Nested
    class Decode {
        @DisplayName("With a valid token")
        @Nested
        class WithValidToken {
            String token;

            @BeforeEach
            void setup () {
                token = jwtUtil.encode(1L);
            }

            @DisplayName("returns claims")
            @Test
            void returnsClaims() {
                Claims claims = jwtUtil.decode(token);

                assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @DisplayName("With an invalid token")
        @Nested
        class WithInvalidToken {
            String invalidToken;

            @BeforeEach
            void setup() {
                invalidToken = "..";
            }

            @DisplayName("throws InvalidTokenException")
            @Test
            void throwsInvalidTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode(invalidToken))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
