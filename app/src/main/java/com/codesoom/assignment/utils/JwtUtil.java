package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.errors.NotSupportedUserIdException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    public static final String USER_ID = "userId";
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String encode(Long userId) {
        if (isNotValidUserId(userId)) {
            throw new NotSupportedUserIdException(userId);
        }

        return Jwts.builder()
                .claim(USER_ID, userId)
                .signWith(key)
                .compact();
    }

    private boolean isNotValidUserId(Long userId) {
        return userId == null || userId <= 0;
    }

    public Long decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            final Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return claims.getBody()
                    .get(USER_ID, Long.class);
        } catch (SignatureException | MalformedJwtException e) {
            throw new InvalidTokenException(token);
        }


    }
}
