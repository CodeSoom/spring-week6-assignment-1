package com.codesoom.assignment.presentation;

import com.codesoom.assignment.application.SessionService;
import com.codesoom.assignment.errors.InvalidTokenException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Authorization 헤더에서 token 가져와서 User 를 리턴한다
 * 유효하지 않은 token 인 경우 InvalidTokenException 을 던진다
 */
public class AccessTokenValidationArgumentResolver implements HandlerMethodArgumentResolver {

    private final SessionService sessionService;

    public AccessTokenValidationArgumentResolver(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AccessTokenValidation.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        String token = parseToken(authorization);
        return sessionService.getUserFromToken(token);
    }

    private String parseToken(String authorization) {
        if (Strings.isBlank(authorization)) {
            throw new InvalidTokenException("Authorization 헤더 값이 없습니다");
        }
        if (!authorization.startsWith("Bearer ")) {
            throw new InvalidTokenException("Authorization 헤더의 토큰이 Bearer 형식이 아닙니다");
        }
        return authorization.substring("Bearer ".length());
    }
}
