package com.codesoom.assignment.utills;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * Jwt에대한 추가기능을 제공합니다.
 */
@Component
public class JwtUtill {
    private final Key key;

    public JwtUtill(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * id를 인코딩된 token으로 변환해 리턴합니다.
     *
     * @param userId 인코딩할 유저 Id
     * @return jwt로 인코딩된 token
     */
    public String encode(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

    /**
     * token을 디코딩해서 id값을 돌려주고 유효하지 않은 토큰이 들어올 경우 예외를 던진다.
     *
     * @param token
     * @return 사용자 id
     * @throws InvalidTokenException 유효하지 않은 토큰이 들어올 경우
     */
    public Claims decode(String token) {
        if (!checkValidToken(token)) {
            throw new InvalidTokenException(token);
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }

    /**
     * token이 유효한 값이면 true, 그렇지 않으면 false 리턴한다.
     *
     * @param token 유저의 token
     * @return 유효한 값일 경우 true, 그렇지 않으면 false
     */
    public boolean checkValidToken(String token) {
        return !(token == null || token.isBlank());
    }
}
