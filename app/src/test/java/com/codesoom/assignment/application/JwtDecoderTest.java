package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JwtDecoderTest {

    private static final String SECRET = "12345678901234567890123456789012";

    private Long userId;
    private String token;

    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        JwtEncoder jwtEncoder = new JwtEncoder(SECRET);

        userId = 1L;

        User user = User.builder()
            .id(userId)
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
                Optional<Claims> claims = jwtDecoder.decode(validToken);

                assertThat(claims.isPresent()).isTrue();

                Long userIdFromClaims = claims.get()
                    .get("userId", Long.class);

                assertThat(userIdFromClaims).isEqualTo(userId);
            }
        }

        @Nested
        @DisplayName("올바르지 않은 토큰이 주어진 경우")
        class Context_invalidToken {

            private final List<String> invalidTokens = new ArrayList<>();

            @BeforeEach
            void setUp() {
                invalidTokens.add(token.substring(0, token.length() - 1));
                invalidTokens.add(token + "a");
                invalidTokens.add(getInvalidTokenByIndex(1));
                invalidTokens.add(getInvalidTokenByIndex(2));
            }

            private String getInvalidTokenByIndex(int index) {
                String[] splitToken = token.split("\\.");

                splitToken[index] = "invalid";

                return String.join(".", splitToken);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws() {
                for (String invalidToken : invalidTokens) {
                    assertThatThrownBy(() -> jwtDecoder.decode(invalidToken))
                        .isInstanceOf(InvalidTokenException.class);
                }
            }
        }
    }
}
