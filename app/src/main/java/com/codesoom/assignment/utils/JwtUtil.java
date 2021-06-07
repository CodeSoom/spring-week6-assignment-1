package com.codesoom.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("jwt.secret") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 사용자 식별자로 JWT를 발급해서 리턴합니다.
     *
     * @param userId 사용자 식별자
     * @return JWT
     */
    public String encode(Long userId) {
        return Jwts.builder()
                   .claim("userId", userId)
                   .signWith(key)
                   .compact();
    }
}
