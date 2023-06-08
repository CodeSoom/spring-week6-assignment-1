package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.LoginFailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SessionServiceTest {

	private SessionService sessionService;


	private final UserService userService = mock(UserService.class);

	private LoginData VALID_LOGIN;
	private LoginData INVALID_LOGIN;

	private final String TOKEN = "a.b.c";

	@BeforeEach
	public void setUp() {
		sessionService = new SessionService(userService, authorizationService);
		VALID_LOGIN = new LoginData("jinny@mail.com", "1234");
		INVALID_LOGIN = new LoginData("jinny@mail.com", "0000");

		given(userService.findUserByEmail(VALID_LOGIN.getEmail()))
				.willReturn(new User());
		given(userService.findUserByEmail(INVALID_LOGIN.getEmail()))
				.willThrow(new LoginFailException(INVALID_LOGIN.getEmail()));


	}

	@Test
	@Description("유효한 유저 로그인")
	public void loginWithValidUser() {
		String token = sessionService.login(VALID_LOGIN);

		assertThat(token).contains(".");

		verify(userService).findUserByEmail(any(), any());
	}

	@Test
	@Description("유효하지 않은 유저 로그인")
	public void loginWithInvalidUser() {
		Assertions.assertThatThrownBy(() -> sessionService.login(INVALID_LOGIN))
				.isInstanceOf(LoginFailException.class);
	}

}
