package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 *  인증에 관련된 로그인 등의 작업을 수행한다
 */
@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login() {
        return jwtUtil.encode(1L);
    }
}
