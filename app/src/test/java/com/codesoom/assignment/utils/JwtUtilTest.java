package com.codesoom.assignment.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtUtil 클래스")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(JwtTestHelper.getSecret());
    }

    @Nested
    @DisplayName("createToken 메소드는")
    class Describe_createToken {

        @Nested
        @DisplayName("아이디가 주어지면")
        class Context_with_id {
            private final Long id = 1L;

            @Test
            @DisplayName("토큰을 생성해 반환한다")
            void it_returns_token() {
                String token = jwtUtil.createToken(id);

                assertThat(JwtTestHelper.hasPattern(token)).isTrue();
            }
        }
    }
}
