package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.UserLoginData;
import com.codesoom.assignment.errors.AuthenticationFailException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    public String login(UserLoginData userLoginData) {
        User user = findUserByEmail(userLoginData);

        if (!user.authenticate(userLoginData.getPassword())) {
            throw new AuthenticationFailException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.encode(1L);
    }

    private User findUserByEmail(UserLoginData userLoginData) {
        return userRepository.findByEmail(userLoginData.getEmail())
                .orElseThrow(() -> new AuthenticationFailException(userLoginData.getEmail()));
    }

    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("userId", Long.class);
    }
}
