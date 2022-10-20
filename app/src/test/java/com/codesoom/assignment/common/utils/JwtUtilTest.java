package com.codesoom.assignment.common.utils;

import com.codesoom.assignment.common.exception.InvalidParamException;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {JwtUtil.class})
@DisplayName("JwtUtil 클래스")
class JwtUtilTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGaa";

    private static final Long USER_ID = 1L;

    @Value("${jwt.secret}")
    private String secret;
    private JwtUtil jwtUtil;

    @BeforeEach
    void prepare() {
        System.out.println("secret = " + secret);
        jwtUtil = new JwtUtil(secret);
    }

    @Nested
    @DisplayName("encode 메소드는")
    class Describe_encode {
        @Nested
        @DisplayName("아이디가 주어지면")
        class Context_with_id {
            @Test
            @DisplayName("JwtToken을 생성하고 리턴한다")
            void it_returns_jwt_token() {
                String accessToken = jwtUtil.encode(USER_ID);

                assertThat(accessToken).isEqualTo(VALID_TOKEN);
            }
        }
    }
    @Nested
    @DisplayName("decode 메소드는")
    class Describe_decode {
        @Nested
        @DisplayName("토큰이 Null이면")
        class Context_with_id {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_jwt_token() {
                assertThatThrownBy(() -> jwtUtil.decode(null)).isInstanceOf(InvalidParamException.class);
                assertThatThrownBy(() -> jwtUtil.decode("  ")).isInstanceOf(InvalidParamException.class);
            }
        }

        @Nested
        @DisplayName("유효하지않은 토큰이 주어지면")
        class Context_with_invalid_token {
            @Test
            @DisplayName("예외를 던진다")
            void it_returns_id() {
                assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN)).isInstanceOf(InvalidParamException.class);
            }
        }

        @Nested
        @DisplayName("유효한 토큰이 주어지면")
        class Context_with_valid_token {
            @Test
            @DisplayName("디코딩 후 아이디를 리턴한다")
            void it_returns_id() {
                Claims claims = jwtUtil.decode(VALID_TOKEN);

                assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
            }
        }


    }

}