package com.codesoom.assignment.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JsonWebToken {
    public static String generate(JwtContents contents) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        String jwt = Jwts.builder()
                .setIssuer(contents.getIss())
                .setSubject(contents.getSub())
                .setAudience(contents.getAud())
                .signWith(key)
                .compact();

        return jwt;
    }
}
