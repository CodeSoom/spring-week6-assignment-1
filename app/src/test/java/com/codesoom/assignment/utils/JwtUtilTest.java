package com.codesoom.assignment.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.codesoom.assignment.Constant.SECRET;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil 클래스")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Nested
    @DisplayName("encode 메서드는")
    class Describe_encode {
        @Nested
        @DisplayName("인자값이 유효한 경우")
        class Context_with_valid_id {
            @DisplayName("토큰이 정상적으로 발급되어 반환된다.")
            @Test
            void encode() {

            }
        }

        @Nested
        @DisplayName("인자값이 유효하지 않을 경우")
        class Context_with_invalid_id {
            @DisplayName("예외가 발생하며 토큰이 발급되지 않는다.")
            @Test
            void encode() {

            }
        }
    }

    @Nested
    @DisplayName("decode 메서드는")
    class Describe_decode {
        @Nested
        @DisplayName("인자값이 유효한 경우")
        class Context_with_valid_token{
            @DisplayName("사용자 정보가 반환된다.")
            @Test
            void decode() {

            }
        }

        @Nested
        @DisplayName("인자값이 유효하지 않을 경우")
        class Context_with_invalid_token {
            @DisplayName("예외가 발생한다.")
            @Test
            void decode() {

            }
        }
    }
}
