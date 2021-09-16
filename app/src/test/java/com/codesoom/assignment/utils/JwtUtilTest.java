package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("JwtUtilTest 클래스")
class JwtUtilTest {

    private static final String SECRET = "01234567reterter8912rfqedc01234567";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.HLQ9r9Devo7ni2MaJIBFWr_t-2GcZWO4H5nYwLjIpJc";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.HLQ9r9Devo7ni2MaJIBFWr_t-2GcZWO4H5nYwLjIsdfsdc";
    private final Long GIVEN_USER_ID = 1L;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }


    @Nested
    @DisplayName("encode메소드는")
    class Describe_encode {

        @Nested
        @DisplayName("user_id가 주어질 때")
        class Context_WithUserId {

            @DisplayName("jwt토큰을 반환한다.")
            @Test
            void It_ReturnsToken() {
                String accessToken = jwtUtil.encode(GIVEN_USER_ID);

                assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode메소드는")
    class Describe_decode {

        @Nested
        @DisplayName("유효한 토큰이 주어질 때")
        class Context_WithValidToken {

            @DisplayName("userId를 반환한다.")
            @Test
            void decodeWithValidToken() {
                Claims claims = jwtUtil.decode(ACCESS_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(GIVEN_USER_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어질 때")
        class Context_WithInValidToken {

            @DisplayName("UnauthorizedException에러를 던진다.")
            @Test
            void decodeWithInvalidToken() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(UnauthorizedException.class);

            }
        }

        @Nested
        @DisplayName("토큰이 없을 때")
        class Context_WithEmpty {

            @DisplayName("UnauthorizedException에러를 던진다.")
            @Test
            void empty_token_decode() {
                assertAll(() -> {
                            assertThatThrownBy(() -> {
                                jwtUtil.decode("");
                            }).isInstanceOf(UnauthorizedException.class);
                        },
                        () -> {
                            assertThatThrownBy(() -> {
                                jwtUtil.decode(" ");
                            }).isInstanceOf(UnauthorizedException.class);
                        },
                        () -> {
                            assertThatThrownBy(() -> {
                                jwtUtil.decode(null);
                            }).isInstanceOf(UnauthorizedException.class);
                        });
            }
        }
    }
}
