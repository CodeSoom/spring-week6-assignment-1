package com.codesoom.assignment.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesoom.assignment.errors.DecodingInValidTokenException;

public class JwtUtilTest {
	private static String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.TysUfytUgm0Kc6zzhOwJPDes3U48wtv9S3qSwnKkkvo";
	private static String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.TysUfytUgm0Kc6zzhOwJPDes3U48wtv9S3qSwnKkkvo1";
	private static String SECRET = "12345678912345678912345678900012";
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
		assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN).get("userId", Long.class))
			.isInstanceOf(DecodingInValidTokenException.class);
	}
}
