package com.codesoom.assignment.utils;

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
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", 1L)
                .signWith(key)
                .compact();
    }
}
