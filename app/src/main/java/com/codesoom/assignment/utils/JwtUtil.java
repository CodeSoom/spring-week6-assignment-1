package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
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
     * JWT 토큰을 생성합니다.
     *
     * @param userId 회원 식별자
     * @return 생성된 토큰
     */
    public String encode(Long userId) {

        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    /**
     * JWT 토큰을 복호화합니다.
     *
     * @param token 토큰
     * @return 복호화된 정보
     * @throws com.codesoom.assignment.errors.InvalidAccessTokenException 토큰이 유효하지 않을 경우
     */
    public Claims decode(String token) throws InvalidAccessTokenException {
        if (token == null || token.isBlank()) {
            throw new InvalidAccessTokenException(token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidAccessTokenException(token);
        }
    }
}
