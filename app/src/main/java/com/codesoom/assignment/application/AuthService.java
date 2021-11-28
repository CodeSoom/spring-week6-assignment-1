package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getToken(User user) {
        return jwtUtil.encode(user.getId());
    }

    public Long parseToken(String token) {
        return jwtUtil.decode(token).get("userId", Long.class);
    }
}
