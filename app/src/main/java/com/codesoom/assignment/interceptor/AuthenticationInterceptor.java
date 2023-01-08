package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (filterWithMethod(request)) {
            return true;
        }

        return doAuthentication(request, response);
    }

    private static boolean filterWithMethod(HttpServletRequest request) {
        String method = request.getMethod();

        return method.equals("GET");
    }

    private boolean doAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            response.sendError(HttpStatus.BAD_REQUEST.value());
            return false;
        }

        String accessToken = authorization.substring("Bearer ".length());

        return authenticationService.isValidToken(accessToken);
    }
}
