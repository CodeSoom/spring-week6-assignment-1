package com.codesoom.assignment.util;

import com.codesoom.assignment.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtil {
	private JwtUtil() {}

	public static String createToken(User user, String secretKey) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

		return Jwts.builder().claim("userId", user.getId()).signWith(key).compact();
	}

	public static Claims decode(String token, String secretKey) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
