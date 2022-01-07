package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtCodec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AuthenticationService 클래스")
class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";
    private static final Long TOKEN_USER_ID = 1L;
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "abc";

    @Nested
    @DisplayName("login 메소드는")
    class Describe_login{

        AuthenticationService authenticationService;

        @BeforeEach
        void setUp(){
            JwtCodec jwtCodec = new JwtCodec(SECRET);
            authenticationService = new AuthenticationService(jwtCodec);
        }

        @Test
        @DisplayName("액세스 토큰을 리턴한다.")
        void login(){
            String accessToken = authenticationService.login(TOKEN_USER_ID);
            assertThat(accessToken).isEqualTo(VALID_TOKEN);
        }
    }

    @Nested
    @DisplayName("parseToken 메소드는")
    class Describe_parse_token{
        
        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_a_valid_token{

            AuthenticationService authenticationService;

            @BeforeEach
            void setUp(){
                JwtCodec jwtCodec = new JwtCodec(SECRET);
                authenticationService = new AuthenticationService(jwtCodec);
            }

            @Test
            @DisplayName("토큰을 디코딩한 id를 리턴한다.")
            void it_returns_decoded_id(){
                Long userId = authenticationService.parseToken(VALID_TOKEN);
                assertThat(userId).isEqualTo(TOKEN_USER_ID);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 토큰이 주어지면")
        class Context_with_a_invalid_token{

            AuthenticationService authenticationService;

            @BeforeEach
            void setUp(){
                JwtCodec jwtCodec = new JwtCodec(SECRET);
                authenticationService = new AuthenticationService(jwtCodec);
            }

            @Test
            @DisplayName("InvalidTokenException 예외를 던진다.")
            void it_returns_string(){
                assertThatThrownBy(
                        () -> authenticationService.parseToken(INVALID_TOKEN)
                ).isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        @DisplayName("빈 토큰이 주어지면")
        class Context_with_a_empty_token{

            AuthenticationService authenticationService;

            @BeforeEach
            void setUp(){
                JwtCodec jwtCodec = new JwtCodec(SECRET);
                authenticationService = new AuthenticationService(jwtCodec);
            }

            @ParameterizedTest(name = "[{index}] InvalidTokenException 예외를 던진다.")
            @NullAndEmptySource
            @ValueSource(strings = {"  ", "\t", "\n"})
            void it_returns_string(String emptyToken){
                assertThatThrownBy(
                            () -> authenticationService.parseToken(emptyToken)
                    ).isInstanceOf(InvalidTokenException.class);
            }
        }

    }
}


