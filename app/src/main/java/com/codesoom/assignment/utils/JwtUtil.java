package com.codesoom.assignment.utils;

import java.security.Key;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String secret;
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") final String secret) {
        this.secret = secret;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(final Long userId) {
        return Jwts.builder()
            .claim("userId", 1L)
            .signWith(key)
            .compact();
    }

    public Claims decode(final String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (SignatureException ignored) {
            throw new InvalidTokenException(token);
        }
    }

}
