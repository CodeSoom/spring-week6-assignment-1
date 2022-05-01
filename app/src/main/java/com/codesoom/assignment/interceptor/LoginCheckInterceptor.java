package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.errors.UnauthorizedException;
import com.codesoom.assignment.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.StringUtils.*;

@Slf4j
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        if (shouldCheckAuthorization(method)) {
            String authorization = request.getHeader("Authorization");

            if (!hasText(authorization)) {
                throw new UnauthorizedException("인증 정보가 없습니다. 인증 정보가 필요합니다.");
            }

            final int prefixLength = "Bearer ".length();
            final String token = authorization.substring(prefixLength);
            Claims claims;

            try {
                claims = jwtUtil.decode(token);
            } catch (JwtException e) {
                throw new UnauthorizedException("잘못된 인증 토큰입니다.");
            }

            Long userId = claims.get("userId", Long.class);
            log.info("login user Id: {}", userId);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean shouldCheckAuthorization(String method) {
        return method.equals(HttpMethod.POST.name()) ||
                method.equals(HttpMethod.PATCH.name()) ||
                method.equals(HttpMethod.DELETE.name());
    }
}
