package com.codesoom.assignment.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    public String encode(Long userId) {
        String secret = "12345678901234567890123456789012";
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claim("userId", 1)
                .signWith(key)
                .compact();
    }
}
