package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Map;

public class JWT {
    private final SecretKey secretKey;

    public JWT(@Value("jwt.secret") String secret) {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(Map<String, Object> headers, Map<String, Object> claims) {
        return Jwts.builder()
                .setHeaderParams(headers)
                .addClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}
