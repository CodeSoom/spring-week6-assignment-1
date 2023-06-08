package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

	private final UserService userService;
	private final AuthorizationService authorizationService;

	public SessionService(UserService userService, AuthorizationService authorizationService) {
		this.userService = userService;
		this.authorizationService = authorizationService;
	}

	/**
	 * 1. login 정보로 일치하는 유저 정보 조회 - UserService
	 * 있으면 -> token 생성 후 리턴
	 * 없으면 -> LoginFailException
	 */
	public String login(LoginData login) {
		try {
			User user = userService.findUserByEmail(login.getEmail());
			if (!user.getPassword().equals(login.getPassword())) {
				throw new LoginFailException(login.getEmail());
			}

			// 토큰 생성
			String token = authorizationService.login(user);

			return token;
		} catch (UserNotFoundException e) {
			throw new LoginFailException(login.getEmail());
		}
	}
}
