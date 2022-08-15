package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 요청이 도달하기 전에 인가 정보를 확인하는 역할을 가지고 있습니다.
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (filterUri(request)) {
            return true;
        }

        if (filterMethod(request)) {
            return true;
        }

        return validateToken(request);
    }

    private boolean validateToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        authenticationService.parseToken(authorization);
        return true;
    }

    private boolean filterMethod(HttpServletRequest request) {
        String method = request.getMethod();
        if ("GET".equals(method)) {
            return true;
        }
        return false;
    }

    private boolean filterUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (!uri.startsWith("/products")) {
            return true;
        }
        return false;
    }
}
