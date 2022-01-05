package com.codesoom.assignment.application;

import com.codesoom.assignment.utils.JwtCodec;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtCodec jwtCodec;

    public AuthenticationService(JwtCodec jwtCodec) {
        this.jwtCodec = jwtCodec;
    }

    public String login(){
        return jwtCodec.encode(1L);
    }

    public Long parseToken(String accessToken) {
        Claims claims = jwtCodec.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
