package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * userId의 토큰을 생성하고 리턴한다.
     *
     * @param userId user의 id
     * @return user의 토큰
     */
    public String encode(Long userId) {

        return Jwts.builder()
                .claim("userId", 1L)
                .signWith(key)
                .compact();
    }

    /**
     * 토큰을 decode하고 리턴한다.
     *
     * @param token user의 토큰
     * @throws InvalidTokenException 예외를 던진다.
     * @return user의 id
     */
    public Claims decode(String token) {
        if(token == null || token.isBlank()){
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
