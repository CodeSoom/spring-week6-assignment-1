package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthenticationService {
    JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String login() {
        return jwtUtil.encode(1L);
    }

}
