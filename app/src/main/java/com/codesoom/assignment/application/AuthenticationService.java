package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginInfoData;
import com.codesoom.assignment.errors.InvalidEmailException;
import com.codesoom.assignment.errors.InvalidPasswordException;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public AuthenticationService(final JwtUtil jwtUtil, final UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String login(final LoginInfoData loginInfoData) {
        final User user = userRepository.findByEmail(loginInfoData.getEmail())
            .orElseThrow(InvalidEmailException::new);
        if (!user.authenticate(loginInfoData.getPassword())) {
            throw new InvalidPasswordException();
        }
        return jwtUtil.encode(user.getId());
    }

    public Long parseToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new InvalidTokenException(accessToken);
        }
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
