package com.codesoom.assignment.config;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.UserRepository;
import com.codesoom.assignment.fake.InMemoryUserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestWebConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret);
    }

    @Bean
    public UserRepository userRepository() {
        return InMemoryUserRepository.getInstance();
    }

    @Bean
    public AuthenticationService authenticationService() {
        return new AuthenticationService(userRepository(), jwtUtil());
    }
}
