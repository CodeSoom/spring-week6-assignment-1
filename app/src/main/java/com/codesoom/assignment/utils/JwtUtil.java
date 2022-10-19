package com.codesoom.assignment.utils;

import com.codesoom.assignment.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * JWT를 관리합니다.
 */
@Component
@Slf4j
public class JwtUtil {

    private static final String USER_KEY = "userId";

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 토큰을 생성합니다.
     * @param userId 회원 아이디
     * @return 생성된 토큰
     * @throws IllegalArgumentException 회원 아이디가 없는 경우
     */
    public String createToken(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("토큰 생성시 회원 아이디가 필요합니다.");
        }

        return Jwts.builder()
                .claim(USER_KEY, userId)
                .signWith(key)
                .compact();
    }

    /**
     * 토큰의 유효성을 확인합니다.
     * @param token 토큰
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

        } catch (SecurityException
                 | MalformedJwtException
                 | ExpiredJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }

    /**
     * 토큰에서 회원 아이디를 가져옵니다.
     * @param token 토큰
     * @return 회원 아이디
     * @throws InvalidTokenException 토큰에서 회원 정보를 찾지 못한 경우
     */
    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        try {
            return claims.get(USER_KEY, Long.class);
        } catch (RequiredTypeException e) {
            log.error(e.getMessage());
            throw new InvalidTokenException("토큰에서 회원 정보를 찾을 수 없습니다.");
        }
    }
}
