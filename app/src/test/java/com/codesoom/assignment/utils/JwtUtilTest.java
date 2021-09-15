package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.NotSupportedUserIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codesoom.assignment.ConstantForAuthenticationTest.SECRET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtil 클래스")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {
        @Nested
        @DisplayName("인자값이 유효한 경우")
        class Context_with_valid_id {
            @DisplayName("토큰을 반환한다.")
            @ParameterizedTest
            @ValueSource(longs = {1, 3, 15, 500})
            void encode(Long userId) {
                final String token = jwtUtil.encode(userId);

                assertThat(token).contains(".");
            }
        }

        @Nested
        @DisplayName("인자값이 유효하지 않을 경우")
        class Context_with_invalid_id {
            @DisplayName("예외를 던진다.")
            @ParameterizedTest
            @NullSource
            @ValueSource(longs = {-10, -1, 0})
            void encode(Long userId) {
                assertThatThrownBy(() -> jwtUtil.encode(userId))
                        .isInstanceOf(NotSupportedUserIdException.class);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {
        @Nested
        @DisplayName("인자값이 유효한 경우")
        class Context_with_valid_token {
            private String validToken;
            private Long userId;

            @BeforeEach
            void setUp() {
                userId = 1L;
                validToken = jwtUtil.encode(userId);
            }

            @DisplayName("사용자 정보를 반환한다.")
            @Test
            void decode() {
                final Long decodedUserId = jwtUtil.decode(validToken);

                assertThat(decodedUserId).isEqualTo(userId);
            }
        }

        @Nested
        @DisplayName("인자값이 유효하지 않을 경우")
        class Context_with_invalid_token {

            @DisplayName("예외를 던진다.")
            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"a.b.c", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGaa"})
            void decode(String invalidToken) {

                assertThatThrownBy(() -> jwtUtil.decode(invalidToken))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
