package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtCodec 클래스")
class JwtCodecTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final Long TOKEN_USER_ID = 1L;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "abc";

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode{

        @Nested
        @DisplayName("id가 주어지면")
        class Context_with_a_user_id{

            JwtCodec jwtCodec;
            Long givenUserId;

            @BeforeEach
            void setUp(){
                jwtCodec = new JwtCodec(SECRET);
                givenUserId = TOKEN_USER_ID;
            }

            @Test
            @DisplayName("id를 인코딩한 토큰을 리턴한다.")
            void it_returns_token(){
                String accessToken = jwtCodec.encode(givenUserId);
                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }

        }
    }

    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode{

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_a_valid_token{

            JwtCodec jwtCodec;
            String validToken;

            @BeforeEach
            void setUp(){
                jwtCodec = new JwtCodec(SECRET);
                validToken = VALID_TOKEN;
            }

            @Test
            @DisplayName("토큰을 디코딩한 id를 리턴한다.")
            void it_returns_decoded_id(){
                Claims claims = jwtCodec.decode(validToken);
                assertThat(claims.get("userId", Long.class)).isEqualTo(TOKEN_USER_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_a_invalid_token{

            JwtCodec jwtCodec;
            String invalidToken;

            @BeforeEach
            void setUp(){
                jwtCodec = new JwtCodec(SECRET);
                invalidToken = INVALID_TOKEN;
            }

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다.")
            void it_returns_string(){
                assertThatThrownBy(() -> jwtCodec.decode(INVALID_TOKEN))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("빈 토큰이 주어지면")
        class Context_with_a_empty_token{

            JwtCodec jwtCodec;

            @BeforeEach
            void setUp(){
                jwtCodec = new JwtCodec(SECRET);
            }

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다.")
            void it_returns_string(){
                Stream.of("", "   ", null).forEach((emptyToken) -> {
                    assertThatThrownBy(() -> jwtCodec.decode(emptyToken))
                            .isInstanceOf(InvalidTokenException.class);
                });
            }
        }
    }
}
