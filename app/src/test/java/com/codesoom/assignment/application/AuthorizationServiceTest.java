package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.LoginData;
import com.codesoom.assignment.dto.LoginSuccessData;
import com.codesoom.assignment.errors.InvalidAccessTokenException;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.UserNotFoundException;
import com.codesoom.assignment.fixture.FixtureData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
	private final String VALID_TOKEN = FixtureData.AccessTokenFixture.VALID_TOKEN.getToken();
	private final String SIGNATURE_FAIL_TOKEN = FixtureData.AccessTokenFixture.SIGNATURE_FAIL_TOKEN.getToken();
	public final String USER_NOT_EXISTS_TOKEN = FixtureData.AccessTokenFixture.USER_NOT_EXISTS_TOKEN.getToken();
	public final Long NOT_EXISTS_USER_ID = FixtureData.AccessTokenFixture.USER_NOT_EXISTS_TOKEN.getUserId();




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

		given(userService.findUser(NOT_EXISTS_USER_ID)).willThrow(new UserNotFoundException(NOT_EXISTS_USER_ID));
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
		String authorization = "Bearer " + SIGNATURE_FAIL_TOKEN;

		assertThatThrownBy(() -> authorizationService.checkUserAuthorization(authorization)).isInstanceOf(
				InvalidAccessTokenException.class);
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "Bearer "})
	@Description("토큰검증_빈토큰_InvalidAccessTokenException")
	void palindromes(String candidate) {
		assertThatThrownBy(() -> authorizationService.checkUserAuthorization(candidate)).isInstanceOf(
				InvalidAccessTokenException.class);
	}

	@Test
	@Description("토큰검증_존재하지않는유저_InvalidAccessTokenException")
	public void notExistUserAuthorization() {
		String authorization = "Bearer " + USER_NOT_EXISTS_TOKEN;

		assertThatThrownBy(() -> authorizationService.checkUserAuthorization(authorization)).isInstanceOf(
				InvalidAccessTokenException.class);
	}

}
