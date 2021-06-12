package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;
    private final Long expiredLength;

    public JwtUtil(@Value("${jwt.secret}")String secret, @Value("${jwt.exp-length}")Long expiredLength) {
        this.key =  Keys.hmacShaKeyFor(Base64.encodeBase64(secret.getBytes()));
        this.expiredLength = expiredLength;
    }

    /**
     * 사용자 아이디로 JSON Web Token을 생성한다.
     * @param userId 사용자아이디
     * @return Token
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }


    /**
     * JSON Web Token을 받아 Claims를 반환한다.
     * @param token JSON Web Token
     * @return Claims
     */
    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException signatureException) {
            throw new InvalidTokenException(token);
        }
    }
}
