package com.codesoom.assignment.utils;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.exception.DecodingInValidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(Long userId, Role role) {
        return Jwts.builder().claim("userId", userId).claim("role", role).signWith(key).compact();
    }

    public Claims decode(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SecurityException e) {
            throw new DecodingInValidTokenException(token); // when decode invalid token
        }
    }
}
