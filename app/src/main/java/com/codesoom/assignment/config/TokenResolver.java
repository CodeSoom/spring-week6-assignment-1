package com.codesoom.assignment.config;

import com.codesoom.assignment.application.auth.InvalidTokenException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/** 요청 헤더에 입력된 인증 토큰을 메서드 파라미터에 바인딩 합니다. */
@Component
public class TokenResolver implements HandlerMethodArgumentResolver {

    private static final String PREFIX = "Bearer ";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAccessTokenAnnotation = parameter.getParameterAnnotation(AccessToken.class) != null;
        boolean isString = parameter.getParameterType().equals(String.class);
        return hasAccessTokenAnnotation && isString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String token = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(token == null) {
            throw new InvalidTokenException("토큰을 입력하세요.");
        }
        if (!token.startsWith(PREFIX)) {
            throw new InvalidTokenException("지원하지 않는 토큰 타입입니다.");
        }

        String accessToken = token.substring(PREFIX.length());
        return accessToken;
    }

}
