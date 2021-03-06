package com.codesoom.assignment.global.config;

import com.codesoom.assignment.auth.infra.JwtTokenProvider;
import com.codesoom.assignment.auth.ui.interceptor.authorization.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(jwtTokenProvider))
                .addPathPatterns("/products", "/products/*");
    }
}
