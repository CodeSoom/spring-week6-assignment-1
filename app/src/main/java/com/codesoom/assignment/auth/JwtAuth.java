package com.codesoom.assignment.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtAuth implements ClaimTokenAuth<Claims> {
    private final SecretKey key;

    public JwtAuth(JwtKey key) {
        this.key = key;
    }

    @Override
    public String encode(Long id) {
        return Jwts.builder()
                .signWith(key.hashed())
                .claim("userId", id)
                .compact();
    }

    @Override
    public Claims decode(String token) {
        return null;
    }
}
