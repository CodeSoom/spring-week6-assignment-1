package com.codesoom.assignment.utils;

import com.codesoom.assignment.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtUtil 클래스")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(JwtTestHelper.getSecret());
    }

    @Nested
    @DisplayName("createToken 메소드는")
    class Describe_createToken {

        @Nested
        @DisplayName("아이디가 주어지면")
        class Context_with_id {
            private final Long id = 1L;

            @Test
            @DisplayName("토큰을 생성해 반환한다")
            void it_returns_token() {
                String token = jwtUtil.createToken(id);

                assertThat(JwtTestHelper.hasPattern(token)).isTrue();
            }
        }

        @Nested
        @DisplayName("아이디가 주어지지 않으면")
        class Context_without_id {

            @Test
            @DisplayName("예외를 던진다")
            void it_returns_token() {
                assertThatThrownBy(() -> jwtUtil.createToken(null))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("validateToken 메소드는")
    class Describe_validateToken {

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {
            private String token = jwtUtil.createToken(1L);

            @Test
            @DisplayName("문제없이 수행된다")
            void it_happens_nothing() {
                jwtUtil.validateToken(token);
            }
        }

        @Nested
        @DisplayName("null이나 화이트 스페이스가 주어지면")
        class Context_with_null_or_white_space {

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"\n", "\t", " "})
            @DisplayName("예외를 던진다")
            void it_throws_exception(String token) {
                assertThatThrownBy(() -> jwtUtil.validateToken(token))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("파싱할 수 없는 값이 주어지면")
        class Context_with_wrong_format {

            @ParameterizedTest
            @ValueSource(strings = {
                    "a", "a.a", "a.a.a", "bbb.b.bb", "23.232.33",
                    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjQ0fQ...eAG4-BS7VDgku9pNxKwyQHqom8EOd3qmBeDOudK2g0s",
                    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjQ0fQ.eAG4-BS7VDgku9pNxKwyQHqom8EOd3qmBeDOudK2g0s11111",
                    "eyJhbGciOJ9.eyJ1ciOjQ0fQ.eAG4OudK2g0s"
            })
            @DisplayName("예외를 던진다")
            void it_throws_exception(String token) {
                assertThatThrownBy(() -> jwtUtil.validateToken(token))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }

    @Nested
    @DisplayName("getUserId 메소드는")
    class Describe_getUserId {

        @Nested
        @DisplayName("회원을 찾을 수 있는 토큰이 주어지면")
        class Context_with_valid_token {
            private final Long id = 1L;
            private String token = jwtUtil.createToken(id);

            @Test
            @DisplayName("회원 아이디를 반환한다")
            void it_returns_user_id() {
                Long userId = jwtUtil.getUserId(token);

                assertThat(userId).isEqualTo(id);
            }
        }
    }
}
