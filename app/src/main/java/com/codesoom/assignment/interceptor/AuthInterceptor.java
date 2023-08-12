package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.errors.AccessTokenNotFoundException;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        isGetMethod(request);
        if (isPostMethod(request) || isPatchMethod(request) || isDeleteMethod(request)) {
            checkAccessToken(request);
        }

        return true;
    }

    private boolean isGetMethod(HttpServletRequest request) {
        return request.getMethod().equals("GET");
    }

    private boolean isPostMethod(HttpServletRequest request) {
        return request.getMethod().equals("POST");
    }

    private boolean isPatchMethod(HttpServletRequest request) {
        return request.getMethod().equals("PATCH");
    }

    private boolean isDeleteMethod(HttpServletRequest request) {
        return request.getMethod().equals("DELETE");
    }

    private boolean checkAccessToken(HttpServletRequest request) throws InvalidAccessTokenException, AccessTokenNotFoundException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) throw new AccessTokenNotFoundException();
        String accessToken = authorization.substring("Bearer ".length());
        try {
            jwtUtil.decode(accessToken);
        } catch (Exception e) {
            throw new InvalidAccessTokenException(e.getMessage());
        }
        return true;
    }

}
