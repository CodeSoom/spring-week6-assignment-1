package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.dto.LoginDto;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.exception.NoRightPasswordException;
import com.codesoom.assignment.exception.UserNotFoundException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private JwtUtil jwtUtil;

    private UserRepository userRepository;

    public AuthenticationService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public SessionResponseData login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(loginDto.getEmail()));

        if (user.getPassword() != loginDto.getPassword()) {
            throw new NoRightPasswordException(loginDto.getEmail());
        }
        return SessionResponseData.builder().accessToken(jwtUtil.encode(user.getId(), Role.MEMBER)).build();
    }

    public Claims decode(String token) {
        return jwtUtil.decode(token);
    }

    public Long parseUserId(String token) {
        return jwtUtil.decode(token).get("userId", Long.class);
    }

    public Role parseUserRole(String token) {
        return jwtUtil.decode(token).get("ROLE", Role.class);
    }
}
