package com.codesoom.assignment.infra;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;

public class JwtUtils {

    public String createToken() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Jwts.builder().signWith(key).compact();
    }
}
