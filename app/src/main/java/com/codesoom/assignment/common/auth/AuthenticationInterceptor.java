package com.codesoom.assignment.common.auth;

import com.codesoom.assignment.common.util.JwtUtil;
import com.codesoom.assignment.session.exception.TokenNotExistException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthenticationInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        if (isNotHandlerMethod(handler) || isNotRequiredAuth(handler)) {
            return true;
        }

        if (isNotExistAuthorization(request)) {
            throw new TokenNotExistException();
        }

        validateAuthToken(request);

        return true;
    }

    private void validateAuthToken(HttpServletRequest request) {
        jwtUtil.validateAccessToken(request.getHeader(HttpHeaders.AUTHORIZATION));
    }

    private static boolean isNotExistAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) == null;
    }

    private static boolean isNotHandlerMethod(Object handler) {
        return !(handler instanceof HandlerMethod);
    }

    private boolean isNotRequiredAuth(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Login auth = handlerMethod.getMethodAnnotation(Login.class);

        return auth == null || !auth.required();
    }
}
