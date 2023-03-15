package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class JwtUtilTest {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJ1c2VySWQiOjF9.bOQfc4mYhcmbCH6JjPuzSc6eRJEr_A0d8tVrPtxE9aU";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UiLCJ1c2VySWQiOjF9.bOQfc4mYhcmbCH6JjPuzSc6eRJEr_A0d8tVrPtxE9a0";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    @DisplayName("encode는 토큰을 반환한다")
    public void encode() throws Exception{
        // when
        String accessToken = jwtUtil.enCode(1L);

        // then
        assertThat(accessToken).isEqualTo(VALID_TOKEN);
        System.out.println(accessToken+"====================");
    }
    @Test
    @DisplayName("Valid_Token")
    public void decodeWithValidToken() throws Exception{
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("userId",Long.class)).isEqualTo(1L);
    }
//    @Test
//    @DisplayName("Invalid_Token")
//    public void decodeWithInvalidToken() throws Exception{
//        assertThatThrownBy(()->jwtUtil.decode(INVALID_TOKEN))
//                .isInstanceOf(SignatureException.class);
//
//    }

//    @Test
//    @DisplayName("jjwt를 이용한 기본적인 예외")
//    public void decodeWithBlankToken() throws Exception{
//        assertThatThrownBy(()->jwtUtil.decode(""))
//                .isInstanceOf(IllegalArgumentException.class);
//    }

    @Test
    @DisplayName("custom Invalid Exception")
    public void decodeWithInvalidAccessToken() throws Exception{
        assertThatThrownBy(()->jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("decodeWithEmptyToken")
    public void decodeWithEmptyToken() throws Exception{
        assertThatThrownBy(()->jwtUtil.decode(null))
                .isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(()->jwtUtil.decode(""))
                .isInstanceOf(InvalidTokenException.class);
        assertThatThrownBy(()->jwtUtil.decode("   "))
                .isInstanceOf(InvalidTokenException.class);
    }

}



