package com.codesoom.assignment.utils;

import com.codesoom.assignment.application.auth.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private final Key KEY;
    private static final String CLAIM_KEY = "userId";

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * 인코딩 된 토큰을 생성해 반환합니다.
     */
    public String encode(Long id) {
        return Jwts.builder()
                .claim(CLAIM_KEY, id)
                .signWith(KEY)
                .compact();
    }

    /**
     * 주어진 토큰의 유효성을 검증 후 Claims를 반환합니다.
     *
     * @param token 유효성을 검증할 토큰
     * @return 유효한 토큰에 담긴 정보
     * @throws InvalidTokenException 토큰 검증에 실패한 경우
     */
    public Claims decode(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | IllegalArgumentException | MalformedJwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }

}
