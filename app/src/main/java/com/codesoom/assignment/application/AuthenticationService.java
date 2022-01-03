package com.codesoom.assignment.application;

import com.codesoom.assignment.utills.JwtUtill;
import org.springframework.stereotype.Service;

/**
 * 사용자 권한을 관리
 */
@Service
public class AuthenticationService {
    private JwtUtill jwtUtill;

    public AuthenticationService(JwtUtill jwtUtill) {
        this.jwtUtill = jwtUtill;
    }

    public String login() {
        return jwtUtill.encode(1L);
    }
}
