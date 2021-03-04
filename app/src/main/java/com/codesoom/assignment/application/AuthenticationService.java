package com.codesoom.assignment.application;

<<<<<<< HEAD
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String login(String email) {
        final Long userId = findUserByEmail(email).getId();
        return jwtUtil.encode(userId);
    }

    public Long parseToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new InvalidAccessTokenException(accessToken);
        }

        try {
            Claims claims = jwtUtil.decode(accessToken);
            return claims.get("userId", Long.class);
        } catch (SignatureException e){
            throw new InvalidAccessTokenException(accessToken);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
