package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.exception.DecodingInValidTokenException;
import com.codesoom.assignment.utils.Permission;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class Interceptor implements HandlerInterceptor {
    private AuthenticationService authenticationService;

    public Interceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Permission permission = handlerMethod.getMethodAnnotation(Permission.class);

        if (permission == null) {
            return true;
        }

        String token = request.getHeader("Authorization").substring("Bearer ".length());

        try {
            Role role = authenticationService.parseUserRole(token);
            if (role != Role.MEMBER) {
                throw new DecodingInValidTokenException(token);
            }
        } catch (DecodingInValidTokenException e) {
            throw new DecodingInValidTokenException(token);
        }
        if (token == null) {
            throw new DecodingInValidTokenException(token);
        }
        return true;
    }
}
