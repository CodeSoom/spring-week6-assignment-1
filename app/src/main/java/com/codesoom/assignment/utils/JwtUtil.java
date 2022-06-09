package com.codesoom.assignment.utils;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.codesoom.assignment.errors.DecodingInValidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final Key key;

	public JwtUtil(@Value("${jwt.secret}") String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String encode(Long userId) {
		return Jwts.builder().claim("userId", userId).signWith(key).compact();
	}

	public Claims decode(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			throw new DecodingInValidTokenException(token);
		}
	}
}
