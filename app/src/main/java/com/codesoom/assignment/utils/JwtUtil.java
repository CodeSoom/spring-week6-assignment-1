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

    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(this.key)
                .compact();
    }

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
