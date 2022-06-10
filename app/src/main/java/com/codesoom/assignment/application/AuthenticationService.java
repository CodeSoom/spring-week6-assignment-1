package com.codesoom.assignment.application;

import org.springframework.stereotype.Service;

import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.utils.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class AuthenticationService {
	private JwtUtil jwtUtil;

	public AuthenticationService(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	public SessionResponseData login(Long userId) {
		return SessionResponseData
			.builder()
			.accessToken(jwtUtil.encode(userId))
			.build();
	}

	public Claims decode(String token) {
		return jwtUtil.decode(token);
	}

	public Long parseUserId(String token) {
		return jwtUtil.decode(token).get("userId", Long.class);
	}
}
