package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtDecoder {

    private final Key key;

    public JwtDecoder(@Value("#{environment['jwt.secret']}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 토큰을 해석하여 클레임을 리턴.
     *
     * @param token 해석할 토큰
     * @return 클레임
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    public Optional<Claims> decode(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();

        try {
            jwtParser.parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new InvalidTokenException();
        }

        return Optional.ofNullable(jwtParser
            .parseClaimsJws(token)
            .getBody());
    }
}
