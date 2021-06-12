package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * Jwt 토큰 관련 운영을 담당합니다.
 */
@Component
public class JwtUtil {

    private final String USER_EMAIL = "userEmail";
    private final Key secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * 사용자 메일과 키로 Jwt 토큰을 발행해 리턴합니다.
     * @param userEmail 사용자 메일
     * @return Jwt 토큰
     */
    public String encode(String userEmail) {
        return Jwts.builder()
                   .claim(USER_EMAIL, userEmail)
                   .signWith(secretKey)
                   .compact();
    }

    /**
     * 토큰을 풀고 Jwt 클레임을 리턴합니다.
     * @param token 풀 토큰
     * @return Jwt 클레임
     */
    public Claims decode(String token) {
        if (token == null || StringUtils.isBlank(token)) {
            throw new InvalidTokenException(token);
        }
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(secretKey)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
