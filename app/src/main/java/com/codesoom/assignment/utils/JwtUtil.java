package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;

public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(Long userId) {
        return Jwts.builder()
<<<<<<< HEAD
                .claim("userId", userId)
=======
                .claim("userId", 1L)
>>>>>>> e4c4a63f623877af12fde4d29074f5ffa6062498
                .signWith(key)
                .compact();
    }

    public Claims decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
