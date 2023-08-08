package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

        /*
        e2e 테스트시
         ... blocked by CORS policy:
         Response to preflight request doesn't pass access control check:
         It does not have HTTP ok status. 오류 발생해서 추가
        */
        if (requestMethod.equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        if(!requestURI.startsWith("/products")){
            return true;
        }

        if(requestMethod.equals(HttpMethod.GET.name())){
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if(authorization==null) throw new InvalidAccessTokenException();
        String accessToken = authorization.substring("Bearer ".length());

        Long userId = authenticationService.parseToken(accessToken);
        return true;
    }
}
