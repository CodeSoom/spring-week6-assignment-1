package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

/**
 * 회원 인증 service 입니다.
 */
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * userId를 encode 하여 jwt 토큰을 리턴합니다.
     *
     * @return jwt 토큰
     */
    public String login() {
        return jwtUtil.encode(1L);
    }

    /**
     * accessToken 을 받아 분해한 결과른 리턴합니다.
     *
     * @param accessToken 분해 할 token 입니다.
     * @return 분해 된 토큰의 정보 입니다.
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
