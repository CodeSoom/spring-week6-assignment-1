package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * JWT 토큰을 관리합니다.
 */
@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 주어진 사용자 ID를 암호화하여 토큰을 발급합니다.
     *
     * @param userId
     * @return 발급된 토큰
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", 1L)
                .signWith(key)
                .compact();
    }

    /**
     * 주어진 토큰을 복호화하여 사용자 정보를 반환합니다.
     *
     * @param token
     * @return 복호화된 정보
     * @throws InvalidTokenException 토큰이 유효하지 않을 경우
     */
    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
