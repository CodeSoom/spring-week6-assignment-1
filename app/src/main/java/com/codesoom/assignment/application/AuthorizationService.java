package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginSuccessData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.util.AuthorizationUtil;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AuthorizationService {
	private final String secretKey;

	private final UserService userService;

	public AuthorizationService(UserService userService, @Value("${jwt.secret}") String secretKey) {
		this.userService = userService;
		this.secretKey = secretKey;
	}

	public LoginSuccessData login(LoginData login) {
		User user = validLoginUserPassword(login);
		String accessToken = JwtUtil.createToken(user, secretKey);

		return new LoginSuccessData(accessToken);
	}

	private User validLoginUserPassword(LoginData login) {
		User user = userService.findUserByEmail(login.getEmail());
		if (!user.authenticate(login.getPassword())) {
			throw new LoginFailException("Password Fail : " + login.getPassword() + " | " + user.getPassword());
		}

		return user;
	}

	public void checkUserAuthorization(String authorization) {
		String token = AuthorizationUtil.extractTokenWithBearer(authorization);
		Long userId = parseToken(token);
		try {
			userService.findUser(userId);
		} catch (UserNotFoundException e) {
			throw new InvalidAccessTokenException(token);
		}
	}

	private Long parseToken(String token) {
		Claims claims = JwtUtil.decode(token, secretKey);

		return claims.get("userId", Long.class);
	}


}
