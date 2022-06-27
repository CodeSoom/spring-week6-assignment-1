package com.codesoom.assignment.helper;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class AuthJwtHelper {
    private final Key key;

    public AuthJwtHelper(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @return userId를 담은 jwt 인증 토큰을 문자열로 반환한다.
     */
    public String encode(Long userId) {
        return encode(userId, null);
    }

    public String encode(Long userId, Date expirationDate) {
        return Jwts.builder()
                .signWith(key)
                .setExpiration(expirationDate)
                .claim("userId", userId)
                .compact();
    }

    /***
     *
     * @return Claims - userId를 담고 있는 claim set 반환
     * @throws InvalidTokenException 유효하지 않은 토큰이 주어졌을 때 발생하는 예외
     */
    public Claims decode(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("토큰 기한이 만료되었습니다.", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("토큰이 null 이거나 빈 토큰 입니다.", e);
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 못한 토큰 입니다.", e);
        }
    }
}

