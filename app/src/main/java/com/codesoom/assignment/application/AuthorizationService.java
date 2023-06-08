package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
	String secretKey;

	public AuthorizationService(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = secretKey;
	}

	public String login(User user) {
		SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

		String token = Jwts.builder().claim("userId", user.getId()).signWith(key).compact();

		return token;
	}
}
