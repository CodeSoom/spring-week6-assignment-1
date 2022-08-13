package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.infra.InMemoryUserRepository;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductControllerTestConfig {
    private static final String SECRET = "12345678901234567890123456789012";

    @Bean
    AuthenticationService authenticationService() {
        return new AuthenticationService(
                new JwtUtil(SECRET),
                new InMemoryUserRepository()
        );
    }
}
