package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.errors.DecodingInValidTokenException;
import com.codesoom.assignment.utils.JwtUtil;

public class AuthenticationServiceTest {
	private AuthenticationService authenticationService;
	private JwtUtil jwtUtil;
	private static String SECRETE = "12345678901234567890123456789010";
	private static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
	private static String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw1";

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil(SECRETE);
		authenticationService = new AuthenticationService(jwtUtil);
	}

	@Test
	void login() {
		SessionResponseData sessionResponseData = authenticationService.login(1L);
		assertThat(sessionResponseData.getAccessToken()).isEqualTo(VALID_TOKEN);
	}

	@Test
	void decodeWithValidToken() {
		Long userId = authenticationService.decode(VALID_TOKEN);
		assertThat(userId).isEqualTo(1L);
	}

	@Test
	void decodeWithInValidToken() {
		assertThatThrownBy(() -> authenticationService.decode(INVALID_TOKEN)).isInstanceOf(
			DecodingInValidTokenException.class);
	}
}
