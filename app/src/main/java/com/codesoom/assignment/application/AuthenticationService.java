package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(Long userId){
        return jwtUtil.encode(userId);
    }

    public Long parseToken(String accessToken) {
        return jwtUtil.decode(accessToken).get("userId", Long.class);
    }
}
