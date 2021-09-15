package com.codesoom.assignment.session.service;

import com.codesoom.assignment.session.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private static final String SECRET = "12345678901234567890123456789012";

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    public String login(Long id) {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        return jwtUtil.encode(id);
    }
}
