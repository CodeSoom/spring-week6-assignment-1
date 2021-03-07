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

    private static final String TOKEN_KEY = "Authorization";
    private static final String BEARER_STRING = "Bearer ";

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (idNonAuthorizationTarget(request)) {
            return true;
        }
        return validateAccessToken(request.getHeader(TOKEN_KEY));
    }

    boolean idNonAuthorizationTarget(HttpServletRequest request) {
        String method = request.getMethod();
        return HttpMethod.GET.matches(method);
    }

    private boolean validateAccessToken(String bearerToken) {
        String accessToken = bearerToken.substring(BEARER_STRING.length());
        authenticationService.decode(accessToken);
        return true;
    }
}
