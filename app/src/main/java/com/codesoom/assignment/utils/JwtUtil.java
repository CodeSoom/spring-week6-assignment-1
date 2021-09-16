package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * JWT토큰을 관리합니다.
 */
@Component
public class JwtUtil {
    private Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    /**
     * JWT토큰을 생성하고 return합니다.
     *
     * @param userId 회원 식별자
     * @return 생성한 토큰
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key).compact();
    }

    /**
     * JWT토큰을 decode하고, decode한 정보를 return합니다.
     *
     * @param token 토큰
     * @return decode한 정보
     * @throws UnauthorizedException 유효하지 않은 토큰일 경우
     */
    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException(token);
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException signatureException) {
            throw new UnauthorizedException(token);
        }
    }
}
