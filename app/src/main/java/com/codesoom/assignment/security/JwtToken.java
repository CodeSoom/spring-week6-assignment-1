package com.codesoom.assignment.security;


import com.codesoom.assignment.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;

public class JwtToken {
    private final Key key;

    public JwtToken(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims decode(String validToken) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(validToken)
                    .getBody();
        } catch (SignatureException e){
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }


    public String encode(Long userId){
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }
}
