package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.NotSupportedIdException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtil 테스트")
class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789010";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드")
    class Describe_encode {

        @Nested
        @DisplayName("유효한 id가 주어지면")
        class Context_with_valid_id {

            private List<Long> givenValidIds;

            @BeforeEach
            void prepare() {
                givenValidIds = List.of(0L, 1L, 999999L, Long.MAX_VALUE, Long.MIN_VALUE);
            }

            @Test
            @DisplayName("암호화된 토큰을 반환한다.")
            void it_return_token() {

                for(Long id : givenValidIds){
                    String token = jwtUtil.encode(id);
                    Claims claims = jwtUtil.decode(token);

                    assertThat(claims.get("userId", Long.class)).isEqualTo(id);
                }
            }
        }

        @Nested
        @DisplayName("Null 값이 주어지면")
        class Context_with_null {

            private Long NULL_VALUE = null;

            @Test
            @DisplayName("NotSupportedIdException을 던집니다.")
            void it_throw_NotSupportedIdException() {
                assertThatThrownBy(()->jwtUtil.encode(NULL_VALUE))
                        .isInstanceOf(NotSupportedIdException.class);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드")
    class Describe_decode {

        @Nested
        @DisplayName("유효한 token이 주어지면")
        class Context_with_valid_token {

            private final Long VALID_ID = 1L;
            private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

            @Test
            @DisplayName("복호화된 Claims을 반환한다.")
            void it_return_claims() {
                Claims token = jwtUtil.decode(VALID_TOKEN);
                assertThat(token.get("userId", Long.class)).isEqualTo(VALID_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않는 데이터가 주어지면")
        class Context_with_invalid_valid {

            private final String NULL_VALUE = null;
            private List<String> givenValues;

            @BeforeEach
            void prepare() {
                givenValues = List.of("", " ");
            }

            @Test
            @DisplayName("InvalidAccessTokenException을 던집니다.")
            void it_throw_InvalidAccessTokenException() {

                for(String value: givenValues){
                    assertThatThrownBy(() -> jwtUtil.decode(value))
                            .isInstanceOf(InvalidAccessTokenException.class);
                }

                assertThatThrownBy(() -> jwtUtil.decode(NULL_VALUE))
                        .isInstanceOf(InvalidAccessTokenException.class);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 token이 주어지면")
        class Context_with_invalid_token {

            private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
                    "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

            @Test
            @DisplayName("InvalidAccessTokenException을 던집니다.")
            void it_throw_InvalidAccessTokenException() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidAccessTokenException.class);
            }
        }
    }
}
