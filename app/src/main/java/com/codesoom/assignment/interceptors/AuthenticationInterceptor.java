package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        if (method.equals("GET")) {
            return true;
        }

        return doAuthentication(request, response);
    }

    private boolean doAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        authenticationService.parseToken(getSubstring(authorization));
        return true;
    }


    private String getSubstring(String authorization) {
        return authorization.substring("Bearer ".length());
    }
}
