package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.infra.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    JwtUtils jwtUtils;
    public String login(LoginRequestData loginRequestData) {
        Map<String, Object> claim = new HashMap<>();
        claim.put("test","123");
        claim.put("id","test123");

        return jwtUtils.createToken(claim);
    }
}
