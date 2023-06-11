package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.errors.AccessTokenExpiredException;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginSuccessData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.util.AuthorizationUtil;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
			throw new LoginFailException("Password Fail");
		}

		return user;
	}

	public void checkUserAuthorization(String authorization) {
		String token = AuthorizationUtil.extractTokenWithBearer(authorization);
		validateToken(token);
	}

	private void validateToken(String token) {
		try {
			Claims claims = parseTokenClaims(token);
			isValidUser(claims);
			isValidTokenExpiration(claims);
		} catch (Exception e) {
			throw new InvalidAccessTokenException(token, e);
		}
	}

	private void isValidTokenExpiration(Claims claims) {
		Date expiration = claims.getExpiration();
		if (expiration != null) {
			if (new Date().before(expiration)) {
				throw new AccessTokenExpiredException(expiration);
			}
		}
	}

	private void isValidUser(Claims claims) {
		Long userId = claims.get("userId", Long.class);
		userService.findUser(userId);
	}

	private Claims parseTokenClaims(String token) {
		return JwtUtil.decode(token, secretKey);
	}
}
