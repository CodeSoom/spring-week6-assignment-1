package com.codesoom.assignment.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;

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
		assertThat(jwtUtil.encode(1L)).isEqualTo(validToken);
	}

	@Test
	void decode() {
		assertThat(jwtUtil.decode(validToken).get("userId", Long.class)).isEqualTo(1L);
	}
}
