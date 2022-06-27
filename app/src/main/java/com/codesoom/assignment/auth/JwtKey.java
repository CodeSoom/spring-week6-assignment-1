package com.codesoom.assignment.auth;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtKey implements SecretKey{
    private final String secretString;
    public JwtKey(@Value("${jwt.secret}") String secretString){
        this.secretString = secretString;
    }
    @Override
    public Key keyEncrypted() {
        return Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }
}
