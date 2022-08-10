package com.codesoom.assignment.application;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.security.SignatureException;


@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    public String login() {
        return jwtUtil.encode(1L);
    }

    public Long parseToken(String accessToken) {

        if(accessToken == null || accessToken.isBlank()){
            throw new InvalidAccessTokenException();
        }
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);

    }
}
