package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginRequestData;
import com.codesoom.assignment.errors.PasswordNotMatchedException;
import com.codesoom.assignment.infra.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public String login(LoginRequestData loginRequestData) {
        String requestEmail = loginRequestData.getEmail();

        User user = userService.findByEmail(requestEmail);
        Map<String, Object> claim = new HashMap<>();

        if (!user.getPassword().equals(loginRequestData.getPassword())) {
            throw new PasswordNotMatchedException(requestEmail);
        }

        return jwtUtils.createToken(claim);
    }
}
