package com.codesoom.assignment.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.errors.DecodingInValidTokenException;
import com.codesoom.assignment.utils.JwtUtil;

import io.jsonwebtoken.Claims;

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

	@Nested
	@DisplayName("decode 메소드는")
	class decode {
		@Nested
		@DisplayName("유저 아이디와 키로 인코딩한 유효한 토큰이 주어졌을 때")
		class withValidToken {
			@Test
			@DisplayName("유저 아이디를 갖고 있는 클레임을 응답한다.")
			void decodeWithValidToken() {
				Claims claims = authenticationService.decode(VALID_TOKEN);
				assertThat(claims.get("userId", Long.class)).isEqualTo(1L);
			}
		}

		@Nested
		@DisplayName("유효하지 않은 토큰이 주어졌을 때")
		class withInValidToken {
			@Test
			@DisplayName("DecodingInValidTokenException 을 응답한다.")
			void decodeWithValidToken() {
				Claims claims = authenticationService.decode(INVALID_TOKEN);
				assertThatThrownBy(() -> authenticationService.decode(INVALID_TOKEN))
					.isInstanceOf(DecodingInValidTokenException.class);
			}
		}
	}
}
