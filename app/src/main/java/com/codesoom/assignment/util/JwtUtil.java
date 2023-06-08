package com.codesoom.assignment.util;

import com.codesoom.assignment.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtil {

	public static String createToken(User user, String secretKey) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

		String token = Jwts.builder().claim("userId", user.getId()).signWith(key).compact();

		return token;
	}
}
