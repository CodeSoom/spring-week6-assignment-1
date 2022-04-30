package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.application.authentication.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationInterceptor implements HandlerInterceptor {
    private AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (filterWithPathAndMethod(request)) {
            return true;
        }

        return doAuthentication(request, response);
    }

    private boolean filterWithPathAndMethod(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.equals("/products")) {
            return true;
        }

        if (method.equals("GET")) {
            return true;
        }

        return false;
    }

    private boolean doAuthentication(HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        String accessToken = authorization.substring("Bearer ".length());
        authenticationService.parseToken(accessToken);
        return true;
    }
}
