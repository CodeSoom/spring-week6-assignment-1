package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private JwtUtil jwtUtil;

    public AuthenticationService(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 사용자 식별자로 JWT를 발급해서 리턴합니다.
     *
     * @return JWT
     */
    public String login() {
        return jwtUtil.encode(1L);
    }
}
