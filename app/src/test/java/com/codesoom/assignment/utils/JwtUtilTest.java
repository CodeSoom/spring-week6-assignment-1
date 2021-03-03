package com.codesoom.assignment.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private static final String SECRET = "12345678901234567890123456789012";

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
                String accessToken = jwtUtil.encode(1L);
                assertThat(accessToken).contains(".");
            }
        }
    }
}
