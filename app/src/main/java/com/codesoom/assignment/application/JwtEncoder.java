package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.AccessToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtEncoder {

    private final Key key;

    public JwtEncoder(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 암호화한 토큰을 리턴합니다.
     *
     * @return 액세스 토큰
     */
    public AccessToken encode() {
        return new AccessToken(Jwts.builder()
            .setSubject("Joe")
            .signWith(key)
            .compact());
    }
}
