package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.util.JwtUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
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
}
