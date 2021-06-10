package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

/**
 * 로그인 로직을 수행합니다.
 */
@Service
public class AuthenticationService {

    JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 사용자 정보로 로그인하여 JWT 를 리턴합니다.
     * @return 생성된 JWT
     */
    public String login() {
        return jwtUtil.encode(1L);
    }

    /**
     * 발행된 JWT 를 복호화 합니다.
     * @param accessToken 로그인하려는 사용자의 JWT
     * @return 사용자 ID
     */
    public Long parseToken(String accessToken) {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
    }
}
