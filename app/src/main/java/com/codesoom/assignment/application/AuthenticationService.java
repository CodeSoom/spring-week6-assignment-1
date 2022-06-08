package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public SessionResponseData login(Long userId) {
        return SessionResponseData
                .builder()
                .accessToken(jwtUtil.encode(userId))
                .build();
    }
}
