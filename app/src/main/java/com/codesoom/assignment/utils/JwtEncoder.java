package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;

/**
 * JWT 토큰의 생성과, 디코딩을 담당합니다.
 */
public class JwtEncoder {

    private final Key key;

    public JwtEncoder(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 유저 id를 입력받아 JWT 토큰을 생성하고 리턴합니다.
     *
     * @param id 유저 id
     * @return JWT Token
     */
    public String encode(Long id) {
        return Jwts.builder().claim("userId", 1L).signWith(key).compact();
    }

    /**
     * 토큰을 입력받아 토큰 생성에 사용된 Claims를 리턴합니다.
     *
     * @param token JWT 토큰
     * @return Claims
     */
    public Claims decode(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
