package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthenticationService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        if(!requestURI.startsWith("/products")){
            return true;
        }

        if(requestMethod.equals(HttpMethod.GET)){
            return true;
        }

        // POST /products, PATCH /products/{id}, DELETE /products/{id}
        String accessToken = request.getHeader("Authorization").substring("Bearer ".length());
        Long userId = authenticationService.parseToken(accessToken);
        return true;
    }
}
