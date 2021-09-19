package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * 인증 서비스 - 토큰 생성 및 검증
 */
@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 토큰을 생성 하여 리턴한다.
     * @param userId
     * @return 토큰
     */
    public String login(Long userId){
        return jwtUtil.encode(userId);
    }

    /**
     * 토큰을 복호화 하여 UserId를 리턴한다.
     * @param accessToken
     * @return UserId
     */
    public Long parseToken(String accessToken) {
        return jwtUtil.decode(accessToken).get("userId", Long.class);
    }
}
