package com.codesoom.assignment.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService implements AuthenticationService {

    public String SECRET_KEY = "12345678901234567890123456789012";

    @Override
    public String create(Long userId) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }
}
