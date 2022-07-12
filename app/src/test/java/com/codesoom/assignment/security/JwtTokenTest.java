package com.codesoom.assignment.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenTest {

    private static final Object USER_ID = 1L;
    private final static String PRIVATE_KEY = "aa";


    private JwtToken jwtToken;
    private String validToken;

    @BeforeEach
    void setup(){
        this.jwtToken = new JwtToken(PRIVATE_KEY);
        this.validToken = jwtToken.encode(USER_ID);
    }

    @DisplayName("토큰을 생성한다.")
    @Test
    void It_encode_token (){
        String token = jwtToken.encode(USER_ID);
        assertThat(token).contains(".");
    }

    @DisplayName("유효한 토큰에 대해서는 디코드에 성공한다.")
    @Test
    void It_decode_valid_token(){
        Claims claims = jwtToken.decode(this.validToken);
        assertThat(claims.get("userId", Long.class)).isEqualTo(USER_ID);
    }


}
