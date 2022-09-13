package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login(){
        return jwtUtil.encode(1L);
    }

    public Claims parseToken(String token) {
        return jwtUtil.decode(token);
    }
}
