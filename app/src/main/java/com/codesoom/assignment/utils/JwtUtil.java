package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Jwt 토큰을 관리한다.
 */
public class JwtUtil {
    private final Key key;
    private final long validTime;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.valid-time}") long validTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.validTime = validTime;
    }

    /**
     * 주어진 사용자 id로 Jwt 토큰을 생성한다.
     *
     * @param userId - 사용자 식별자
     * @return - 생성된 토큰
     */
    public String encode(Long userId) {
        Date now = new Date();
        Date expiration = calculationExpiration(now, validTime);

        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    private Date calculationExpiration(Date now, long validTime) {
        return new Date(now.getTime() + validTime);
    }
}
