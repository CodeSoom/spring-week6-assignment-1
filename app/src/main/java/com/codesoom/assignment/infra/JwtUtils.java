package com.codesoom.assignment.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    String keyValue;

    public JwtUtils(@Value("${jwt.secret}") String keyValue) {
        this.keyValue = keyValue;
    }

    public Claims decode(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String encode(Long id) {
        Key key = createKey();

        Map<String, Object> claim = createClaim(id);

        return Jwts.builder()
                .addClaims(claim)
                .signWith(key)
                .compact();
    }

    private Key createKey() {
        return Keys.hmacShaKeyFor(keyValue.getBytes());
    }

    private Map<String, Object> createClaim(Long id) {
        Map<String, Object> claim = new HashMap<>();
        claim.put("userId", id);
        return claim;
    }
}
