package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.JwtDecoder;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtDecoder jwtDecoder;

    public AuthenticationInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if ("GET".equals(method)) {
            return true;
        }

        if ("OPTIONS".equals(method)) {
            return true;
        }

        if (!path.startsWith("/products")) {
            return true;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || !isValidTokenWith(auth)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }

    private boolean isValidTokenWith(String auth) {
        String token = auth.substring("Bearer ".length());

        return jwtDecoder.decode(token).isPresent();
    }
}
