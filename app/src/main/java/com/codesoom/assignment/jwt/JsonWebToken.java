package com.codesoom.assignment.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JsonWebToken {
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generate(JwtContents contents) {

        String jwt = Jwts.builder()
                .setIssuer(contents.getIss())
                .setSubject(contents.getSub())
                .setAudience(contents.getAud())
                .signWith(key)
                .compact();

        return jwt;
    }

    public static boolean verify(String jwt) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
