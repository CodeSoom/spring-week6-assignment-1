package com.codesoom.assignment.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {
	private static String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.TysUfytUgm0Kc6zzhOwJPDes3U48wtv9S3qSwnKkkvo";
	private static String SECRET = "12345678912345678912345678900012";
	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil(SECRET);
	}

	@Test
	void encode() {
		String jwt = jwtUtil.encode(1L);
		assertThat(jwt).isEqualTo(validToken);
	}

	@Test
	void decode() {
		Long userId = jwtUtil.parseUserId(validToken);
		assertThat(userId).isEqualTo(1L);
	}
}
