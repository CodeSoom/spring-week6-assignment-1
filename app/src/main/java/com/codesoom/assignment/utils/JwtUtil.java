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
 * Jwt 토큰을 관리합니다.
 */
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 주어진 회원 id와 secert key로 Jwt 토큰을 생성합니다.
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
     * 주어진 토큰을 복호화하고 복호화된 정보를 리턴합니다.
     *
     * @param token 토큰
     * @return 복호화된 정보
     * @throws InvalidTokenException 토큰이 유효하지 않을 경우
     */
    public Claims decode(String token) throws InvalidTokenException {
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
