package com.codesoom.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Map;

public class JWT {
    private final SecretKey secretKey;

    public JWT(String key) {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String encode(Map<String, Object> headers, Map<String, Object> claims) {
        return Jwts.builder()
                .setHeaderParams(headers)
                .addClaims(claims)
                .signWith(secretKey)
                .compact();
    }
}
