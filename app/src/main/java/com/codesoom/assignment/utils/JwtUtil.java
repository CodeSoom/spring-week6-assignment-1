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
 * JWT(Json Web Token) 기본 설정과 처리를 담당합니다.
 * 처리된 데이터를 변환하거나 가공하여 반환합니다.
 */
@Component
public class JwtUtil {
    private final Key key;

    /**
     * JwtUtil Contructor, JWT signature 규약을 설정합니다.
     * @param secret JWT secret key
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWT 토큰을 생성합니다.
     * @param userId 회원 식별자
     * @return JWT
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    public Claims decode(String token) {
        if(token == null || token.isBlank()) {
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
