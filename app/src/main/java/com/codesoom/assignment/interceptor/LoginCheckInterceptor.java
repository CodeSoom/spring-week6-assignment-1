package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.annotation.CheckJwtToken;
import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.errors.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private AuthenticationService authenticationService;

    LoginCheckInterceptor(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if(request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }


        try{
            boolean hasAnnotation = checkAnnotation(handler, CheckJwtToken.class);

            if(hasAnnotation){
                String accessToken = getAccessToken(request);
                authenticationService.parseToken(accessToken);
            }
        }catch(InvalidTokenException e){
            request.getRequestDispatcher("/session/error").forward(request, response);
            return false;
        }
        return true;
    }

    // CheckJwtToken 어노테이션 존재여부 체크
    public boolean checkAnnotation(Object handler, Class<CheckJwtToken> authClass){

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(handlerMethod.getMethodAnnotation(authClass) != null){
            return true;
        }

        return false;
    }

    // Header에서 AccessToken 추출
    public String getAccessToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");

        if(authorization == null || authorization.isBlank()){
            throw new InvalidTokenException("");
        }
        return authorization.substring("Bearer ".length());
    }


}
