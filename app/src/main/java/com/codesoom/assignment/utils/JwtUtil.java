package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 *
 *  jwt 토큰을 관리합니다.
 *
 */
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

    public Claims decode(String token) {
        if(token == null || token.isBlank()) {
            throw new InvalidAccessTokenException(token);
        }
        try{
            return  Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch(SignatureException e) {
            throw new InvalidAccessTokenException(token);
        }

    }
}
