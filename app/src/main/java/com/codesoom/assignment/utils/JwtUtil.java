package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.NotSupportedIdException;
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

    public String encode(Long userId) {
        if(!isValidUserId(userId)) {
            throw new NotSupportedIdException(userId);
        }

        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key).compact();
    }

    /**
     * token을 파싱해 claim을 반환합니다.
     *
     * @param token 암호화된 token
     * @return claim
     * @throws InvalidAccessTokenException token이 유효하지 않은 값인 경우 던집니다.
     */
    public Claims decode(String token) {
        if(!isValidToken(token)) {
            throw new InvalidAccessTokenException(token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidAccessTokenException(token);
        }
    }

    public boolean isValidUserId(Long userId) {
        return userId != null;
    }

    public boolean isValidToken(String token) {
        if(token == null) {
            return false;
        }

        return !token.isBlank();
    }
}
