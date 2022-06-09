package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthService;
import com.codesoom.assignment.dto.Authentication;
import com.codesoom.assignment.errors.VerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();

        if (HttpMethod.GET.matches(method)) {
            return true;
        }

        String token = extractToken(request)
                .orElseThrow(() -> new VerificationException("Authorization 헤더에 토큰이 존재하지 않음"));

        Authentication authentication = authService.verify(token);

        request.setAttribute("authentication", authentication);

        return true;
    }


    private Optional<String> extractToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            return Optional.empty();
        }

        if (!authorization.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX)) {
            return Optional.empty();
        }

        String token = authorization.substring(AUTHORIZATION_HEADER_VALUE_PREFIX.length());

        return Optional.of(token);
    }
}
