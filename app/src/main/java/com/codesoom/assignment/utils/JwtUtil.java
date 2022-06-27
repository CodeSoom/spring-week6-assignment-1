package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    /**
     * secret 을 받아 key 를 생성한다
     *
     * @param secret key
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 엑세스 토큰을 반환한다
     *
     * @param userId 주어진 userId
     * @param name 주어진 name
     * @param email 주어진 email
     * @return 엑세스 토큰
     */
    public String encode(Long userId, String name, String email) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("name", name)
                .claim("email", email)
                .signWith(key)
                .compact();
    }

    /**
     * 토큰을 해석하여 클레임을 반환한다
     *
     * @param accessToken 해석할 토큰
     * @return 클레임
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    public Claims decode(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (SecurityException e) {
            throw new InvalidTokenException(accessToken);
        }
    }
}
