package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginSuccessData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.fixture.FixtureData;
import io.jsonwebtoken.security.SignatureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthorizationServiceTest {

	private final UserService userService = mock(UserService.class);
	private AuthorizationService authorizationService;

	private final LoginData VALID_LOGIN = FixtureData.LOGIN_VALID;
	private final LoginData LOGIN_NOT_EXIST = FixtureData.LOGIN_NOT_EXIST;
	private final LoginData LOGIN_PW_FAIL = FixtureData.LOGIN_PW_FAIL;
	private final String VALID_TOKEN = FixtureData.VALID_TOKEN;
	private final String INVALID_TOKEN = FixtureData.INVALID_TOKEN;



	@BeforeEach
	public void setUp() {
		String secret = FixtureData.SECRET_KEY;
		authorizationService = new AuthorizationService(userService, secret);

		given(userService.findUserByEmail(VALID_LOGIN.getEmail()))
				.willReturn(User.builder()
						.id(2L)
						.email("jinny@mail.com")
						.password("1234")
						.build());
		given(userService.findUserByEmail(LOGIN_NOT_EXIST.getEmail()))
				.willThrow(new UserNotFoundException(LOGIN_NOT_EXIST.getEmail()));

	}

	@Test
	@Description("로그인_성공_토큰반환")
	public void loginWithValidUser() {
		LoginSuccessData loginSuccessData = authorizationService.login(VALID_LOGIN);

		assertThat(loginSuccessData.getAccessToken()).contains(".");

		verify(userService).findUserByEmail(any());
	}

	@Test
	@Description("로그인_존재하지않는 유저_UserNotFoundException")
	public void loginWithInvalidUser() {
		Assertions.assertThatThrownBy(() -> authorizationService.login(LOGIN_NOT_EXIST))
				.isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@Description("로그인_틀린패스워드_LoginFailException")
	public void loginWithFailPasswordUser() {
		Assertions.assertThatThrownBy(() -> authorizationService.login(LOGIN_PW_FAIL))
				.isInstanceOf(LoginFailException.class);
	}

	@Test
	@Description("토큰검증_성공_토큰유저조회")
	public void validAuthorization() {
		String authorization = "Bearer " + VALID_TOKEN;

		authorizationService.checkUserAuthorization(authorization);

		verify(userService).findUser(any());
	}

	@Test
	@Description("토큰검증_잘못된시그니처_SignatureException")
	public void invalidAuthorization() {
		String authorization = "Bearer " + INVALID_TOKEN;

		assertThatThrownBy(() -> authorizationService.checkUserAuthorization(authorization)).isInstanceOf(
				SignatureException.class);
	}

	@Test
	@Description("토큰검증_빈토큰_InvalidAccessTokenException")
	public void blankAuthorizationToken() {
		String authorization = "Bearer ";

		assertThatThrownBy(() -> authorizationService.checkUserAuthorization(authorization)).isInstanceOf(
			InvalidAccessTokenException.class);
	}

	@Test
	@Description("토큰검증_빈토큰_InvalidAccessTokenException")
	public void blankAuthorization() {
		String authorization = "";

		assertThatThrownBy(() -> authorizationService.checkUserAuthorization(authorization)).isInstanceOf(
			InvalidAccessTokenException.class);
	}

}
