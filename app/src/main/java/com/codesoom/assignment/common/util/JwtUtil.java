package com.codesoom.assignment.common.util;

import com.codesoom.assignment.session.exception.InvalidTokenException;
import com.codesoom.assignment.session.exception.TokenNotExistException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private static final String ACCESS_TOKEN_TYPE = "Bearer";
    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public void validateAccessToken(String authHeader) {
        if (authHeader == null) {
            throw new TokenNotExistException();
        }

        if (!authHeader.startsWith(ACCESS_TOKEN_TYPE)) {
            throw new InvalidTokenException();
        }

        decode(authHeader.substring(ACCESS_TOKEN_TYPE.length()));
    }

    public String encode(Long id) {
        return Jwts.builder()
                .claim("userId", id)
                .signWith(secretKey)
                .compact();
    }

    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException exception) {
            throw new InvalidTokenException(token);
        }
    }
}
