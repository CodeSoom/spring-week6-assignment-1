package com.codesoom.assignment.utils;

import com.codesoom.assignment.dto.SessionTokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 유저 식별자를 암호화해서 토큰을 발급합니다.
     *
     * @param userId 유저 식별자
     * @return 토큰
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(this.key)
                .compact();
    }

    /**
     * 토큰을 해독하고 그 데이터를 반환합니다.
     *
     * @param token 인가 토큰
     * @return 해독된 토큰의 데이터
     */
    public SessionTokenData decode(String token) {
        Claims claims =  Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return SessionTokenData.builder()
                .userId(claims.get("userId", Long.class))
                .build();
    }
}
