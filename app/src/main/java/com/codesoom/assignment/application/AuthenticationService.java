package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 토큰을 생성한다.
     * @return Access 토큰
     */
    public String login() {
        return jwtUtil.encode(1L);
    }

    /**
     * Token을 받으면 사용자 아이디를 반환한다.
     * @param accessToken Access 토큰
     * @return 사용자 아이디
     */
    public Long parseToken(String accessToken){
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
