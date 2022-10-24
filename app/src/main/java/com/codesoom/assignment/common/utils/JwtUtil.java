package com.codesoom.assignment.common.utils;

import com.codesoom.assignment.common.exception.InvalidParamException;
import com.codesoom.assignment.common.response.ErrorCode;
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
     * userId를 받아서 jwtToken을 생성한다.
     * @param userId 사용자 ID
     * @return 생성된 토큰
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    /**
     * token을 받아서 복호화한다.
     * @param token 토큰
     * @return 복호화된 Body
     */
    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidParamException(ErrorCode.INVALID_TOKEN);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidParamException(ErrorCode.INVALID_TOKEN);
        }
    }

}
