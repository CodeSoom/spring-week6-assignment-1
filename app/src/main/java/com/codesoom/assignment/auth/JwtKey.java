package com.codesoom.assignment.auth;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class JwtKey implements SecretKey{
    private final String secretString;
    public JwtKey(@Value("${jwt.secret}") String secretString){
        this.secretString = secretString;
    }
    @Override
    public Key hashed() {
        return Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }
}
