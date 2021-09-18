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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
            @ValueSource(longs = {1, 3, 15, 500, 5000, 10000, 500000,
                    1000000, 9999999, 999999999999L, Integer.MAX_VALUE, Long.MAX_VALUE})
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
            @ValueSource(longs = {Long.MIN_VALUE, Integer.MIN_VALUE, -99999999999L, -999999, -9999, -10, -1, 0})
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
            List<SessionInfoPair> sessionInfoPairs;


            @BeforeEach
            void setUp() {
                sessionInfoPairs = LongStream.range(1, 20)
                        .mapToObj(index -> new SessionInfoPair(index, jwtUtil.encode(index)))
                        .collect(Collectors.toList());
            }

            /**
             * 유효한 인자값과 토큰 객체
             */
            private class SessionInfoPair {
                private final Long id;
                private final String token;

                public SessionInfoPair(Long id, String token) {
                    this.id = id;
                    this.token = token;
                }

                public Long getId() {
                    return id;
                }

                public String getToken() {
                    return token;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) {
                        return true;
                    }
                    if (!(o instanceof SessionInfoPair)) {
                        return false;
                    }
                    SessionInfoPair that = (SessionInfoPair) o;
                    return Objects.equals(id, that.id) && Objects.equals(token, that.token);
                }

                @Override
                public int hashCode() {
                    return Objects.hash(id, token);
                }
            }

            @DisplayName("사용자 정보를 반환한다.")
            @Test
            void decode() {
                for (SessionInfoPair sessionInfoPair : sessionInfoPairs) {
                    final String token = sessionInfoPair.getToken();
                    final Long decodedId = jwtUtil.decode(token);

                    assertThat(decodedId).isEqualTo(sessionInfoPair.getId());
                    assertThat(new SessionInfoPair(decodedId, jwtUtil.encode(decodedId)))
                            .isEqualTo(sessionInfoPair);


                }
            }
        }

        @Nested
        @DisplayName("인자값이 유효하지 않을 경우")
        class Context_with_invalid_token {

            @DisplayName("예외를 던진다.")
            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {
                    "a.b.c",
                    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGaa",
                    "eyJhbGciOiJIUzI1NiJ9",
                    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9",
                    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9....neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw"
            })
            void decode(String invalidToken) {

                assertThatThrownBy(() -> jwtUtil.decode(invalidToken))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }
}
