package com.codesoom.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * jwt 토큰 관련 기능을 제공합니다.
 */
@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 회원의 Id로 jwt 토큰을 생성합니다.
     * @param userId 발행할 유저의 아이디
     * @return 유저에게 할당할 토큰
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }
}
