package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtil 테스트")
class JwtUtilTest {
    private static final String SECRET = "01234567890123456789012345678901";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjEsIm5hbWUiOiLqs73tmJXsobAiLCJlbWFpbCI6InJoa3JndWR3aEB0ZXN0LmNvbSJ9." +
            "dMHJNEhJClh3l3bV6vV1ckS77iTHzlGWHT22Mq_DiLg";
    private static final String INVALID_TOKEN = VALID_TOKEN + "0";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {

        private final Long USER_ID = 1L;
        private static final String NAME = "곽형조";
        private static final String EMAIL = "rhkrgudwh@test.com";

        @Nested
        @DisplayName("유저 아이디, 이름, 이메일을 받으면")
        class Context_with_userId_and_email {

            @Test
            @DisplayName("jwt 토큰을 생성하여 리턴한다")
            void it_return_jwt_token() {
                String token = jwtUtil.encode(USER_ID, NAME, EMAIL);

                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {

        @Nested
        @DisplayName("토큰이 유효하다면")
        class Context_valid_token {

            @Test
            @DisplayName("토큰을 decode 할 수 있다")
            void it_decode() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("토큰이 유효하지 않다면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("비어있는 토큰이라면")
        class Context_with_blank_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(""))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("공백으로 이루어진 토큰이라면")
        class Context_with_whitespace_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> jwtUtil.decode("  "))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("토큰이 null이라면")
        class Context_with_null_token {

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다")
            void it_throw_exception() {
                assertThatThrownBy(() -> jwtUtil.decode(null))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }
    }


}