package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.utils.JwtUtils;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements AuthenticationService {
    private final JwtUtils jwtUtils;

    public JwtService(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String login(UserLoginData data) {
        return jwtUtils.encode(data.getEmail());
    }
}
