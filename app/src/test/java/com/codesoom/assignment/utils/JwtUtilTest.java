package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private final String SECRET = "12345678901234567890123456789010";
    private final JwtUtil jwtUtil = new JwtUtil(SECRET);
    private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    private final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWTESTQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    @Test
    void encode() {
        String token = jwtUtil.encode(1L);
        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Nested
    @DisplayName("encode()")
    class Describe_Encode{

        @Nested
        @DisplayName("파라미터가 null이라면")
        class Context_NullParameter{

            @Test
            @DisplayName("페이로드 부분은 비어있다.")
            void It_EmptyPayload(){
                String payload = jwtUtil.encode(null).split("\\.")[1];
                assertThat(payload).isEqualTo("");
            }
        }
    }

    @Nested
    @DisplayName("decode()")
    class Describe_Decode{

        @Nested
        @DisplayName("토큰이 공백, null또는 유효하지 않은 토큰이라면")
        class Context_BlankOrNullToken{

            @Test
            @DisplayName("예외를 던진다.")
            void It_ThrowException(){
                assertThatThrownBy(() -> jwtUtil.decode(""))
                        .isInstanceOf(InvalidTokenException.class);

                assertThatThrownBy(() -> jwtUtil.decode(null))
                        .isInstanceOf(InvalidTokenException.class);

                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("정상 토큰을 디코딩한다면")
        class Context_DecodeWithValidToken{

            @Test
            @DisplayName("페이로드를 반환한다.")
            void It_ReturnPayload(){
                Claims claims = jwtUtil.decode("Bearer " + VALID_TOKEN);
                assertThat(claims.get("userId" , Long.class)).isEqualTo(1L);
            }
        }
    }

}
