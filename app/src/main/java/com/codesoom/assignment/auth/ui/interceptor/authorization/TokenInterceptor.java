package com.codesoom.assignment.auth.ui.interceptor.authorization;

import com.codesoom.assignment.auth.application.InvalidTokenException;
import com.codesoom.assignment.auth.infra.AuthorizationExtractor;
import com.codesoom.assignment.auth.infra.AuthorizationType;
import com.codesoom.assignment.auth.infra.JwtTokenProvider;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (HttpMethod.GET.matches(request.getMethod())) {
            return true;
        }
        final String token = extractToken(request);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(token);
        }
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }
}
