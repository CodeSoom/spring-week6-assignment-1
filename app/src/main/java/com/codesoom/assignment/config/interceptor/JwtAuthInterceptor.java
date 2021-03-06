package com.codesoom.assignment.config.interceptor;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.errors.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * JWT 토큰 인증이 필요한 HTTP 요청의 사전 처리를 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    private static final String TOKEN_KEY = "Authorization";
    private static final String PREFIX = "Bearer ";

    /**
     * HTTP 요청의 인증 토큰이 유효한지 확인 합니다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param handler 실행할 핸들러
     * @return {@code true} 다음 실행 체인을 진행하려면
     * @throws InvalidTokenException 토큰이 유효하지 않을 경우
     * @throws IOException 입력 또는 출력에 문제가 있을 경우
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws InvalidTokenException, IOException {
        if (filterWithMethods(request, HttpMethod.GET)) {
            return true;
        }

        return doAuthentication(request, response);
    }

    private boolean filterWithMethods(HttpServletRequest request,
                                      HttpMethod... httpMethods) {
        return Arrays.stream(httpMethods)
                .anyMatch(httpMethod -> httpMethod.matches(request.getMethod()));
    }

    private boolean doAuthentication(HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {
        String authorization = request.getHeader(TOKEN_KEY);

        if (authorization == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return true;
        }

        String accessToken = authorization.substring(PREFIX.length());
        authenticationService.parseToken(accessToken);

        return true;
    }

}
