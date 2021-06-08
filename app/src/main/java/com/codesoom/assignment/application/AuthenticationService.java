package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * 자원에 접근할 수 있는 권한을 관리합니다.
 */
@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 자원에 접근할 수 있는 권한을 발급하고 반환합니다.
     *
     * @return 자원에 접근할 수 있는 권한을 부여하는 토큰
     */
    public String login() {
        return this.jwtUtil.encode(1L);
    }
}
