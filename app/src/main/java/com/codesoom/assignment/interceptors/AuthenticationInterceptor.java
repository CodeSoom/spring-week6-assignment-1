package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.AuthorizationService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

	private final AuthorizationService authorizationService;

	public AuthenticationInterceptor(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		return filterWithPathAndMethod(request) || doAuthentication(request, response);
	}

	private boolean filterWithPathAndMethod(HttpServletRequest request) {
		String path = request.getRequestURI();
		String method = request.getMethod();

		if (!path.startsWith("/product")) {
			return true;
		}
		if (method.equals("GET")) {
			return true;
		}

		return false;
	}

	private boolean doAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String authorization = request.getHeader("Authorization");

		if (authorization == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		authorizationService.checkUserAuthorization(authorization);
		return true;
	}
}
