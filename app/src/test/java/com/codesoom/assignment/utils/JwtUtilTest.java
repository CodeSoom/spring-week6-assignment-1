package com.codesoom.assignment.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.exception.DecodingInValidTokenException;

class JwtUtilTest {
	private static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
	private static String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw1";
	private static String SECRET = "12345678901234567890123456789010";
	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil(SECRET);
	}

	@Test
	void encodeToken() {
		assertThat(jwtUtil.encode(1L)).isEqualTo(VALID_TOKEN);
	}

	@Test
	void decodeWithValidToken() {
		assertThat(jwtUtil.decode(VALID_TOKEN).get("userId", Long.class)).isEqualTo(1L);
	}

	@Test
	void decodeWithInValidToken() {
		assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
			.isInstanceOf(DecodingInValidTokenException.class);
	}
}
