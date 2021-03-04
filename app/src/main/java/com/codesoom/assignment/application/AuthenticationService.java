package com.codesoom.assignment.application;

import com.codesoom.assignment.dto.AuthenticationCreateData;
import com.codesoom.assignment.dto.AuthenticationResultData;
import com.codesoom.assignment.dto.SessionResultData;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public SessionResultData login(AuthenticationCreateData authenticationCreateData) {
        String accessToken = jwtUtil.encode(
                authenticationCreateData.getEmail()
                , authenticationCreateData.getPassword()
        );

        return SessionResultData.builder()
                .accessToken(accessToken)
                .build();
    }

    public AuthenticationResultData parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);

        return AuthenticationResultData.builder()
                .email(claims.get("email", String.class))
                .password((claims.get("password", String.class)))
                .build();
    }
}
