package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtEncoder {
    private final Key key;

    public JwtEncoder(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String encode(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 가 필요합니다");
        }
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    public Claims decode(String token) {
        if (Strings.isBlank(token)) {
            throw new InvalidTokenException("토큰이 비어 있습니다");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException("유효하지 않은 토큰 입니다");
        }
    }
}
