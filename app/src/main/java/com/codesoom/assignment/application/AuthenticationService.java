package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.LoginRequestData;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthenticationService {
    public String login(LoginRequestData loginRequestData) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Jwts.builder().signWith(key).compact();
    }
}
