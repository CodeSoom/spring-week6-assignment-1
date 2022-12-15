package com.codesoom.assignment.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private static final Integer VALIDITY_PERIOD = 1800000; //30분
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_PERIOD))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("ERROR-01 SignatureException: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("ERROR-02 ExpiredJwtException: " + e.getMessage());
        } catch (RuntimeException e) {
            log.error("ERROR-03 RuntimeException: " + e.getMessage());
        } catch (Exception e) {
            log.error("ERROR-04 Exception: " + e.getMessage());
        }
        return false;
    }
}
