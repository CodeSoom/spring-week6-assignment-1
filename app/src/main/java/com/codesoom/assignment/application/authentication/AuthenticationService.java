package com.codesoom.assignment.application.authentication;

import com.codesoom.assignment.common.JwtUtil;
import com.codesoom.assignment.domain.user.User;
import com.codesoom.assignment.domain.user.UserRepository;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    final private JwtUtil jwtUtil;
    final private UserRepository userRepository;

    @Autowired
    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException());

        if (!user.authenticate(password)) {
            throw new LoginFailException();
        }

        return jwtUtil.encode(1L);
    }

    public Long parseToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new InvalidAccessTokenException();
        }

        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
