package com.codesoom.assignment.utils;

import com.codesoom.assignment.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret){
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWT를 생성한다.
     *
     * @param userId JWT의 페이로드에 담길 정보
     */
    public String encode(Long userId){
        return Jwts.builder()
                .claim("userId" , userId)
                .signWith(key)
                .compact();
    }

    /**
     * JWT를 디코딩한다.
     *
     * @param token 디코딩할 토큰 정보
     * @throws InvalidTokenException 토큰 정보가 사이즈가 0이거나 공백 또는 유효하지 않은 토큰이라면 예외를 던진다.
     * @return Claims JWT의 페이로드 부분을 반환
     */
    public Claims decode(String token) {
        if(StringUtils.isEmpty(token) || token.isBlank()){
            throw new InvalidTokenException(token);
        }
        try{
            token = token.replace("Bearer " , "");
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch(SignatureException e){
            throw new InvalidTokenException(token);
        }
    }

    /**
     * JWT에서 사용자 식별자를 반환한다.
     *
     * @param token 추출할 토큰 정보
     * @throws InvalidTokenException 토큰 정보가 null 또는 사이즈가 0이거나 첫 글자가 공백 , 유효하지 않은 토큰이라면 예외를 던진다.
     * @return Long 사용자 식별자
     */
    public Long getUserIdFromToken(String token){
        Claims payload = decode(token);
        return payload.get("userId" , Long.class);
    }
}
