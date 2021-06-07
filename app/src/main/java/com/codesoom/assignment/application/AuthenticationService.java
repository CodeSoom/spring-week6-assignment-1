package com.codesoom.assignment.application;

import com.codesoom.assignment.uitls.JwtUtil;
import org.springframework.stereotype.Service;

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
