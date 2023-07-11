package com.codesoom.assignment.util;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthorizationUtilTest {

	@Test
	void bearerToken_success() {
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
		String authorization = "Bearer " + token;

		String gainToken = AuthorizationUtil.extractTokenWithBearer(authorization);

		assertThat(gainToken).isEqualTo(token);
	}

	@Test
	void bearerToken_empty_fail() {
		String token = "";
		String authorization = "Bearer " + token;

		assertThatThrownBy(() -> AuthorizationUtil.extractTokenWithBearer(authorization))
				.isInstanceOf(InvalidAccessTokenException.class);
	}

}
