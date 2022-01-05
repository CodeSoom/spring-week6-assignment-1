package com.codesoom.assignment.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthenticationService {
    public String login() {
        String secret = "01234567890123456789012345678901";
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claim("userId", 1)
                .signWith(key)
                .compact();
    }
}
