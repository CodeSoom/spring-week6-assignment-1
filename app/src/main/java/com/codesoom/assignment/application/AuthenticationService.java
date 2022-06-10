package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * JWT 관련 비즈니스를 처리하는 Service 클래스
 */
@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * userId로 토큰을 만들고, 이를 반환한다.
     * 
     * @return 토큰
     */
    public String login() {
        return jwtUtil.encode(1L);
    }
}
