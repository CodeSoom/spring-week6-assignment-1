package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.SessionRegistrationData;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String login(SessionRegistrationData registrationData) {
        User user = userRepository.findByEmail(registrationData.getEmail())
                .orElseThrow(() -> new UserNotFoundException(registrationData.getEmail()));

        return jwtUtil.encode(user.getId());
    }
}
