package com.codesoom.assignment.common.resolver;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.common.exception.InvalidParamException;
import com.codesoom.assignment.common.response.ErrorCode;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.regex.Pattern;

@Slf4j
@Generated
@Component
public class TokenDecodeResolver implements HandlerMethodArgumentResolver {
    private final AuthenticationService authenticationService;

    public TokenDecodeResolver(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final boolean isTokenAnnotation = parameter.getParameterAnnotation(AccessToken.class) != null;
        final boolean isAuthUser = parameter.getParameterType().equals(AuthUser.class);
        return isTokenAnnotation && isAuthUser;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = request.getHeader("Authorization");

        if (Strings.isBlank(authorization)) {
            throw new InvalidParamException(ErrorCode.INVALID_TOKEN);
        }

        if (isNotExistToken(authorization)) {
            throw new InvalidParamException(ErrorCode.INVALID_TOKEN);
        }

        String accessToken = authorization.replaceAll("^Bearer( )*", "");

        Long userId = authenticationService.parseToken(accessToken);

        if (isNotExistUserId(userId)) {
            throw new InvalidParamException(ErrorCode.INVALID_TOKEN);
        }

        return new AuthUser(userId);
    }

    private boolean isNotExistToken(String authorization) {
        return !Pattern.matches("^Bearer .*", authorization);
    }

    private boolean isNotExistUserId(Long userId) {
        return userId == null || authenticationService.isNotExistId(userId);
    }
}
