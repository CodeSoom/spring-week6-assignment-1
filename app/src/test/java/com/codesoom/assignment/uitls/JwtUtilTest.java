package com.codesoom.assignment.uitls;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtil 클래스")
class JwtUtilTest {
    private JwtUtil jwtUtil;

    private static final String SECRET = "gkskenfdutkeidktjeifnturldksiekt";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9" +
            ".5vWKIu_dVvDx0_K39RSWmrfkyNNsae0lJE3HauIMI5I";
    private static final String INVALID_TOKEN = VALID_TOKEN.replace("5", "6");
    private static final Long REGISTERED_ID = 1L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {

        @Nested
        @DisplayName("적합한 정보를 받으면")
        class Context_with_valid_source {

            @Test
            @DisplayName("토큰을 생성합니다.")
            void it_encode_right_token() {
                String token = jwtUtil.encode(REGISTERED_ID);

                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {

        @Nested
        @DisplayName("적합한 토큰을 받으면")
        class Context_with_valid_token {

            @Test
            @DisplayName("토큰을 해독한 값을 리턴합니다.")
            void it_return_decoded_data() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(REGISTERED_ID);
            }
        }

        @Nested
        @DisplayName("적합하지 않은 토큰을 받으면")
        class Context_with_invalid_token {

            @Test
            @DisplayName("SignatureExcepiton을 던집니다.")
            void it_return_SignatureException() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(SignatureException.class);
            }
        }
    }
}
