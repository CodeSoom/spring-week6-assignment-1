package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utills.JwtUtill;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
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
        if (accessToken.isEmpty()) {
            //TODO isBlank로 변경 java 버전 변경
            throw new InvalidAccessTokenException(accessToken);
        }
        try {
            Claims claims = jwtUtill.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidAccessTokenException(accessToken);
        }
    }
}
