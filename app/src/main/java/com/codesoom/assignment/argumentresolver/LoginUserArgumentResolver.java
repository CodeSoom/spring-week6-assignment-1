package com.codesoom.assignment.argumentresolver;

import com.codesoom.assignment.application.UserService;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginUser;
import com.codesoom.assignment.exception.InvalidTokenException;
import com.codesoom.assignment.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 회원 인증을 처리합니다.
 */
@Slf4j
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public LoginUserArgumentResolver(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasLoginUserType = LoginUser.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasLoginUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = resolveToken(request);

        jwtUtil.validateToken(token);

        Long userId = jwtUtil.getUserId(token);
        User user = userService.findUser(userId);
        return new LoginUser(user.getId());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(BEARER)) {
            throw new InvalidTokenException("요청에 토큰이 존재하지 않습니다.");
        }

        return bearerToken.substring(BEARER.length());
    }
}
