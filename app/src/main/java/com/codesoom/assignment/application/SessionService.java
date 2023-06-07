package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

	private UserService userService;

	public SessionService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 1. login 정보로 일치하는 유저 정보 조회 - UserService
	 * 있으면 -> token 생성 후 리턴
	 * 없으면 -> UserNotFoundException
	 */
	public String login(LoginData login) {
		User userByEmailByPassword = userService.findUserByEmailByPassword(login.getEmail(), login.getPassword());

		// todo 구현
		return "a.b.c";
	}
}
