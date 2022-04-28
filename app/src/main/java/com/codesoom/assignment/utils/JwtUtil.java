package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.UserNotFoundException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 *  Jwt 토큰 생성, 유효성 검증 등의 작업을 수행한다
 */
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String encode(Long userId) {
        if(userId == null) throw new UserNotFoundException(userId);
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    public Claims decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
