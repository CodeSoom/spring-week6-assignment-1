package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * jwt 토큰과 관련된 기능입니다.
 */
@Component
public class JwtUtil {
    public final Key key;

    /**
     * secret 을 받아 key 를 생성합니다.
     *
     * @param secret 32Byte 의 문자열 입니다.
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 회원 정보를 인코딩한 JWT 토큰을 리턴합니다.
     *
     * @param userId 회원 id
     * @param name   회원 이름
     * @param email  회원 이메일
     * @return JWT 토큰
     */
    public String encode(Long userId, String name, String email) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("name", name)
                .claim("email", email)
                .signWith(key)
                .compact();
    }

    /**
     * token 을 분해하여 분해된 정보를 리턴합니다.
     *
     * @param token jwt 토큰
     * @return jwt 토큰을 분해한 정보
     * @throws InvalidTokenException 토큰이 유효하지 않을 경우
     *                               토큰이 null 이거나 비어있을 경우
     */
    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
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
}
