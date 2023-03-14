package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    public String login() {

        return jwtUtil.encode(1L);

    }

    public Long parseToken(String accessToken) {

        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);

    }
}
