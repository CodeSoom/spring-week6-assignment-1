package com.codesoom.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 *  JwtUtil은 Jwt 토큰 생성, 유효성 검증 등의 작업을 수행한다
 */
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }
}
