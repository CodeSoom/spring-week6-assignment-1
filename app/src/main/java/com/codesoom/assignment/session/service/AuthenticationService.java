package com.codesoom.assignment.session.service;

import com.codesoom.assignment.session.errors.InvalidTokenException;
import com.codesoom.assignment.session.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
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

    public Long parseToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            Claims claims = jwtUtil.decode(token);
            return claims.get("userId", Long.class);
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
