package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

/**
 * 로그인 인증 토큰(Access token)를 가공하여 반환하거나 처리합니다.
 */
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 로그인 인증토큰을 발급하고 반환합니다.
     * @return JWT 인증토큰
     */
    public String login(Long userId) {
        return jwtUtil.encode(userId);
    }

    /**
     * 로그인 인증토큰을 검증하고 사용자 정보를 반환합니다.
     * @param accessToken 인증토큰
     * @return userId 회원 ID
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
