package com.codesoom.assignment.jwt;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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

    /**
     * 토큰이 유효한지 검증한다. 유효한 경우 토큰 내용물을 리턴한다.
     * @param jwt 검사하려는 토큰
     * @throws InvalidTokenException 토큰이 유효하지 않을 경우
     */
    public static JwtContents verify(String jwt) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);

            return JwtContents.from(jws);
        } catch (Exception e) {
            throw new InvalidTokenException(jwt);
        }
    }
}
