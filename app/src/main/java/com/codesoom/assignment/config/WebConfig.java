package com.codesoom.assignment.config;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.argumentresolver.LoginUserArgumentResolver;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public WebConfig(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(jwtUtil, userService));
    }
}
