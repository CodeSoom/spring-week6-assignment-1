package com.codesoom.assignment.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configurable
public class WebJavaConfig implements WebMvcConfigurer {
    private HandlerInterceptor authenticationIntercepter;

    public WebJavaConfig(HandlerInterceptor authenticationIntercepter) {
        this.authenticationIntercepter = authenticationIntercepter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationIntercepter);
    }
}
