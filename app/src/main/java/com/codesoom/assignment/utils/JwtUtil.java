package com.codesoom.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * JWT를 관리합니다.
 */
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWT를 생성합니다.
     * @param userId 회원 아이디
     * @return 생성된 Token
     * @throws IllegalArgumentException 회원 아이디가 없는 경우
     */
    public String createToken(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("토큰 생성시 회원 아이디가 필요합니다.");
        }

        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }
}
