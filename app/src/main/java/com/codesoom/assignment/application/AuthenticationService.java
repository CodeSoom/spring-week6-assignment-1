package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.LoginFailedException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthenticationService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String login(UserLoginData userLoginData) {
        String email = userLoginData.getEmail();
        if (!userRepository.existsByEmail(email)) {
            throw new UserNotFoundException(email);
        }

        User user = findUser(userLoginData);

        return jwtUtil.createToken(user.getId());
    }

    public Long decodeToken(String accessToken) {
        Claims claims = jwtUtil.getClaims(accessToken);
        return claims.get("userId", Long.class);
    }

    private User findUser(UserLoginData userLoginData) {
        return userRepository.findByEmailAndPassword(userLoginData.getEmail(), userLoginData.getPassword())
                .stream()
                .findFirst()
                .orElseThrow(() -> new LoginFailedException(userLoginData.getEmail()));
    }
}
