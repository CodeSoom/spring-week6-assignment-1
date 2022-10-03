package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <Client>
 * Authentication -> 로그인 -> Token (인증)
 * Token -> Authorization (인가)
 *
 * <Server>
 * Token -> Authentication = 로그인 (인증)
 * User -> Role -> Authorization (인가)
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        String method = request.getMethod();
        if(method.equals("GET")){
            return true;
        }
        if(authorization == null || authorization.isBlank()){
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        authenticationService.tokenValidation(authorization);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
