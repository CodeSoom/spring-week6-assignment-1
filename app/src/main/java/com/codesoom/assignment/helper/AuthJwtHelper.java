package com.codesoom.assignment.helper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class AuthJwtHelper {
    private final Key key;

    public AuthJwtHelper(@Value("{jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @return userId를 담은 jwt 인증 토큰을 문자열로 반환한다.
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .signWith(key)
                .claim("userId", userId)
                .compact();
    }
}
