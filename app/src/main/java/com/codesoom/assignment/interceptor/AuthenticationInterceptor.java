package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(filterWithMethodAndPath(request)) {
            return true;
        }
        return doAuthentication(request, response);
    }

    private boolean filterWithMethodAndPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if(!path.startsWith("/products")) {
            return true;
        }

        if(method.equals("GET")) {
            return true;
        }

        return false;
    }

    private boolean doAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String Authorization = request.getHeader("Authentication").substring("Bearer ".length());
        System.out.println("Authentication :" + Authorization);

        if(Authorization == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        String accessToken = request.getHeader("Authentication").substring("Bearer ".length());
        authenticationService.parseToken(accessToken);

        return true;
    }
}
