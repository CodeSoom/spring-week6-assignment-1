package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(UserLoginData loginData) {
        User loginUser = userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email address"));

        if (!loginUser.getPassword().equals(loginData.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return jwtUtil.encode(loginUser.getId());
    }
}
