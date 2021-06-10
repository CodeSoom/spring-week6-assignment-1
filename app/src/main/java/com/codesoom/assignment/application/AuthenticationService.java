package com.codesoom.assignment.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public String login() {
        String secret = "sfldksdf";
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder().signWith(key).claim("userId", 1L).compact();
    }
}
