package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.infra.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthenticationService {
    JwtUtils jwtUtils;
    public String login(LoginRequestData loginRequestData) {
        return jwtUtils.getAccessToken();
    }
}
