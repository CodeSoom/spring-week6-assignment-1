package com.codesoom.assignment.application.auth;

import com.codesoom.assignment.application.users.UserNotFoundException;
import com.codesoom.assignment.domain.users.User;
import com.codesoom.assignment.domain.users.UserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationService implements AuthorizationService {

    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    public UserAuthorizationService(JwtUtil jwtUtil, UserRepository repository) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    @Override
    public Long parseToken(String accessToken) {
        final Claims claims = jwtUtil.decode(accessToken);
        final Long userId = claims.get("userId", Long.class);

        return userId;
    }

}
