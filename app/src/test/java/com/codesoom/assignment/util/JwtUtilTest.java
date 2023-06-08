package com.codesoom.assignment.util;

import com.codesoom.assignment.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

	String SECRET = "12345678901234567890123456789010";

	@Test
	public void createToke() {
		User user = User.builder().id(1L).build();
		String secretKey = SECRET;
		String token = JwtUtil.createToken(user, secretKey);

		assertThat(token).isEqualTo("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw");
	}

}
