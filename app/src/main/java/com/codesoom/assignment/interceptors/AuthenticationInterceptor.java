package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthenticationService;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    private static final String TOKEN_KEY = "Authorization";
    private static final String BEARER_STRING = "Bearer ";

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (isNonAuthorizationTarget(request)) {
            return true;
        }
        return validateAccessToken(response, request.getHeader(TOKEN_KEY));
    }

    boolean isNonAuthorizationTarget(HttpServletRequest request) {
        String method = request.getMethod();
        return HttpMethod.GET.matches(method) || HttpMethod.OPTIONS.matches(method);
    }

    private boolean validateAccessToken(HttpServletResponse response, String bearerToken) throws IOException {
        if (bearerToken == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return true;
        }

        String accessToken = bearerToken.substring(BEARER_STRING.length());
        authenticationService.decode(accessToken);
        return true;
    }
}
