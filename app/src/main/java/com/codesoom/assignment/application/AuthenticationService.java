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

    /**
     * 사용자가 로그인하면 아이디를 인코딩하여 리턴합니다.
     *
     * @return 인코딩된 아이디
     */
    public String login() {
        return jwtUtill.encode(1L);
    }

    /**
     * 인증된 토큰을 받아 아이디를 디코딩하여 리턴합니다.
     *
     * @return 사용자 아이디
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtill.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
