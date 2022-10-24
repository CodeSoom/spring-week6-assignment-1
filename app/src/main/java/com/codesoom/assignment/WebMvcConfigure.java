package com.codesoom.assignment;

import com.codesoom.assignment.application.SessionService;
import com.codesoom.assignment.presentation.AccessTokenValidationArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {
    private final SessionService sessionService;

    public WebMvcConfigure(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accessTokenValidationArgumentResolver());
    }

    @Bean
    public AccessTokenValidationArgumentResolver accessTokenValidationArgumentResolver() {
        return new AccessTokenValidationArgumentResolver(sessionService);
    }
}
