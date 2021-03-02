package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

/**
 * 회원 인증 로직을 처리합니다.
 */
@Service
public class AuthenticationService {

    /**
     * 회원의 access token을 반환합니다.
     *
     * @param user token을 발급할 회원
     * @return JWT Access Token
     */
    public String login(User user) {
        //TODO JWT 분리 예정
        String secret = "12345678901234567890123456789012";
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claim("userId", user.getId())
                .signWith(key)
                .compact();
    }
}
