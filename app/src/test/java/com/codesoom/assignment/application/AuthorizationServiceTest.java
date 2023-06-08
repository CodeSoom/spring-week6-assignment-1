package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.errors.LoginFailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthorizationServiceTest {

	private final UserService userService = mock(UserService.class);
	private AuthorizationService authorizationService;


	private final String SECRET = "12345678901234567890123456789010";
	private LoginData VALID_LOGIN;
	private LoginData INVALID_LOGIN;

	@BeforeEach
	public void setUp() {
		authorizationService = new AuthorizationService(userService, SECRET);
		VALID_LOGIN = new LoginData("jinny@mail.com", "1234");
		INVALID_LOGIN = new LoginData("jinny_no@mail.com", "0000");

		User VALID_USER = User.builder()
				.id(2L)
				.email("jinny@mail.com")
				.password("1234")
				.build();

		given(userService.findUserByEmail(VALID_LOGIN.getEmail()))
				.willReturn(VALID_USER);
		given(userService.findUserByEmail(INVALID_LOGIN.getEmail()))
				.willThrow(new LoginFailException(INVALID_LOGIN.getEmail()));

	}

	@Test
	@Description("유효한 유저 로그인")
	public void loginWithValidUser() {
		String token = authorizationService.login(VALID_LOGIN);

		assertThat(token).contains(".");

		verify(userService).findUserByEmail(any());
	}

	@Test
	@Description("유효하지 않은 유저 로그인")
	public void loginWithInvalidUser() {
		Assertions.assertThatThrownBy(() -> authorizationService.login(INVALID_LOGIN))
				.isInstanceOf(LoginFailException.class);
	}


}
