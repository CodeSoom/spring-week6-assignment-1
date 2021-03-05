package com.codesoom.assignment.config.interceptor;

import com.codesoom.assignment.application.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 토큰 인증이 필요한 HTTP 요청의 사전 처리를 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    private static final String TOKEN_KEY = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (HttpMethod.GET.matches(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader(TOKEN_KEY);
        if (authorization == null) {
            return true;
        }

        String accessToken = authorization.substring(PREFIX.length());
        authenticationService.parseToken(accessToken);

        return true;
    }

}
