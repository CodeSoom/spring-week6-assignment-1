package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {


    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    /**
     * 사용자 식별자로 JWT를 발급해서 리턴합니다.
     *
     * @param userId 사용자 식별자
     * @return JWT
     */
    public String customEncode(Long userId){
        return Jwts.builder().signWith(key).claim("userId", 1L).compact();
    }

    /**
     * accessToken 을 가져와서 사용자 정보를 리턴합니다.
     *
     * @param accessToken 사용자 식별자
     * @return claims
     */
    public Claims customDecode(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

}
