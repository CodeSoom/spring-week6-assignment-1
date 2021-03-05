package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789012";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMinvalid";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode() 메소드는 ")
    class Describe_encode {

        @Nested
        @DisplayName("유저의 아이디를 전달하면")
        class Context_pass_userid {

            @Test
            @DisplayName("토큰을 반환한다")
            void it_return_token() {
                String token = jwtUtil.encode(1L);
                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }
    }

    @Nested
    @DisplayName("decode() 메소드는 ")
    class Describe_decode{

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token{

            @Test
            @DisplayName("클레임을 반환한다.")
            void it_(){
                Claims claims = jwtUtil.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
            }
        }

        @Nested
        @DisplayName("null이나 빈 토큰이 주어지면")
        class Context_with_null_or_empty_token{

            @Test
            @DisplayName("InvalidTokenException을 던진다")
            void it_throw_exception(){
                assertThrows(InvalidTokenException.class,() -> jwtUtil.decode(""));
                assertThrows(InvalidTokenException.class,() -> jwtUtil.decode(" "));
                assertThrows(InvalidTokenException.class,() -> jwtUtil.decode(null));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_invalid_token{

            @Test
            @DisplayName("InvalidTokenException을 던진다")
            void it_throw_exception(){
                assertThrows(InvalidTokenException.class,() -> jwtUtil.decode(INVALID_TOKEN));
            }
        }
    }
}
