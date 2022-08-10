package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtils {
    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 정보를 암호화하고 암호화된 토큰을 리턴합니다.
     *
     * @param email 정보
     * @return 토큰
     */
    public String encode(String email) {
        return Jwts.builder()
                .claim("email", email)
                .signWith(key)
                .compact();
    }

    /**
     * 암호화된 토큰을 복호화합니다.
     *
     * @param inputToken 토큰
     * @return 정보
     * @throws UnAuthorizedException 토큰이 유효하지 않을 때
     */
    public Claims decode(String inputToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(inputToken)
                    .getBody();
        } catch (Exception e) {
            throw new UnAuthorizedException(e.getMessage());
        }
    }
}
