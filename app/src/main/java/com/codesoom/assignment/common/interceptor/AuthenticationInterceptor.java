package com.codesoom.assignment.common.interceptor;

import com.codesoom.assignment.common.exception.InvalidParamException;
import com.codesoom.assignment.common.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final List<LoginRequired> LOGIN_REQUIRED_LIST = new ArrayList<>();


    public AuthenticationInterceptor() {
        LOGIN_REQUIRED_LIST.add(new LoginRequired("POST", "/products"));
        LOGIN_REQUIRED_LIST.add(new LoginRequired("PATCH", "/product/"));
        LOGIN_REQUIRED_LIST.add(new LoginRequired("DELETE", "/products/"));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        System.out.println(pathVariables);
        if (isUnnecessaryLonginCheck(request)) {
            return true;
        }

        String accessToken = request.getHeader("Authorization");

        if (isBlankToken(accessToken)) {
            throw new InvalidParamException(ErrorCode.INVALID_TOKEN);
        } else {
            if (isExistToken(accessToken)) {
                accessToken = accessToken.replaceAll("^Bearer( )*", "");
            }
        }

        return true;
    }

    private boolean isUnnecessaryLonginCheck(HttpServletRequest request) {
        final String reqMethod = request.getMethod();
        final String reqURI = request.getRequestURI();
        long count = LOGIN_REQUIRED_LIST.stream()
                .filter(loginRequired -> reqMethod.equals(loginRequired.getReqMethod())
                        && loginRequired.getReqUri().contains(reqURI))
                .count();

        return count == 0;
    }

    private boolean isBlankToken(String authorization) {
        return authorization == null || authorization.isBlank();
    }

    private boolean isExistToken(String authorization) {
        return Pattern.matches("^Bearer .*", authorization);
    }

    @Getter
    @AllArgsConstructor
    private static class LoginRequired {
        private String reqMethod;
        private String reqUri;
    }
}
