package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT Token 발급/해독을 담당.
 */
@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 주어진 유저 id로 JWT 토큰을 발급하여 반환합니다.
     *
     * @param userId 유저 id
     * @return JWT 토큰
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    /**
     * 주어진 JTW 토큰을 해독하여 유저 id를 반환합니다.
     *
     * @param token JTW Token
     * @return 유저 id
     * @throws InvalidAccessTokenException 토큰이 유효하지 않은 경우
     */
    public Long decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidAccessTokenException(token);
        }
        
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidAccessTokenException(token);
        }
    }
}
