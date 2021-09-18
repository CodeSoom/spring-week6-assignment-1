package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthenticationService {

    @Value("${jwt.secret}")
    String secret;

    public String login(User user){
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
                .claim("userId", user.getId())
                .signWith(key)
                .compact();
    }

    public Claims decode(String token){
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
