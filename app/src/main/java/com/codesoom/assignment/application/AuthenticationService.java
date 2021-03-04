package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
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

    public Long parseToken(String accessToken) {
        if (accessToken.isBlank()) {
            throw new InvalidAccessTokenException(accessToken);
        }

        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e){
            throw new InvalidAccessTokenException(accessToken);
        }
    }
}
