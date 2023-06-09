package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
	String secretKey;

	private final UserService userService;

	public AuthorizationService(UserService userService, @Value("${jwt.secret}") String secretKey) {
		this.userService = userService;
		this.secretKey = secretKey;
	}

	public String login(LoginData login) {
		try {
			User user = userService.findUserByEmail(login.getEmail());
			if (!user.getPassword().equals(login.getPassword())) {
				throw new LoginFailException(login.getEmail());
			}

			return JwtUtil.createToken(user, secretKey);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			throw new UserNotFoundException(login.getEmail());
		}
	}

	public Long parseToken(String authorization) {
		System.out.println("authorization : " + authorization);
		Claims claims = JwtUtil.decode(extractTokenWithBearer(authorization), secretKey);

		return claims.get("userId", Long.class);
	}

	private String extractTokenWithBearer(String authorization) {
		if (authorization == null || StringUtils.isBlank(authorization)) {
			throw new InvalidAccessTokenException(authorization);
		}

		String token = authorization.substring("Bearer ".length());
		if (StringUtils.isBlank(token)) {
			throw new InvalidAccessTokenException(authorization);
		}
		return token;
	}
}
