package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Jwt 토큰을 관리합니다.
 */
@Component
public class JwtUtil {

    private final Key key;

    private final long validTime;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.valid-time}") long validTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.validTime = validTime;
    }

    /**
     * 주어진 회원 id를 이용해 Jwt 토큰을 생성합니다.
     *
     * @param userId 회원 식별자
     * @return 생성된 토큰
     */
    public String encode(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validTime);

        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
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
