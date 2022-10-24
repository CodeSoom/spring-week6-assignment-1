package com.codesoom.assignment.config;

import com.codesoom.assignment.common.resolver.TokenDecodeResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenDecodeResolver tokenDecodeResolver;

    public WebConfig(TokenDecodeResolver tokenDecodeResolver) {
        this.tokenDecodeResolver = tokenDecodeResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenDecodeResolver);
    }
}
