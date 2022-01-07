package com.codesoom.assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

/**
 * JWT 토큰의 생성과, 디코딩을 담당합니다.
 */
public class JwtUtil {
    /**
     * 유저 id를 입력받아 JWT 토큰을 생성하고 리턴합니다.
     *
     * @param id 유저 id
     * @return JWT Token
     */
    public String encode(Long id){
        return null;
    }

    /**
     * 토큰을 입력받아 토큰 생성에 사용된 Claims를 리턴합니다.
     *
     * @param token JWT 토큰
     * @return Claims
     */
    public Claims decode(String token){
        return null;
    }
}
