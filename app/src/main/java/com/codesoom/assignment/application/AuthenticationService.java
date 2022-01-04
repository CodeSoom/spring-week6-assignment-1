package com.codesoom.assignment.application;

import com.codesoom.assignment.utills.JwtUtill;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

/**
 * 사용자 권한을 관리합니다.
 */
@Service
public class AuthenticationService {
    private final JwtUtill jwtUtill;

    public AuthenticationService(JwtUtill jwtUtill) {
        this.jwtUtill = jwtUtill;
    }

    public String login() {
        return jwtUtill.encode(1L);
    }

    public Long parseToken(String accessToken) {
        Claims claims = jwtUtill.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
