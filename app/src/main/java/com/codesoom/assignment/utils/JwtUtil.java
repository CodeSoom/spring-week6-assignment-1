package com.codesoom.assignment.utils;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * Jwt token 관리 유틸
 */
@Component
public class JwtUtil {
//    1. construct with secret and store as a Key
//    2. encode with given id
//    3. decode returns Claims

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode() {
        return null;
    }
}
