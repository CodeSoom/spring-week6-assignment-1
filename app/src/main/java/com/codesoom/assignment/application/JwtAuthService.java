package com.codesoom.assignment.application;

import com.codesoom.assignment.auth.JwtAuth;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.infra.JpaUserRepository;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthService implements TokenAuthService {
    private final JwtAuth auth;
    private final JpaUserRepository repository;

    public JwtAuthService(JwtAuth auth, JpaUserRepository repository) {
        this.auth = auth;
        this.repository = repository;
    }

    @Override
    public String login(User user) {
        return auth.encode(user.getId());
    }
}
