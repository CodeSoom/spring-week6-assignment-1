package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class LoginSuccessData {
	private final String accessToken;

	public LoginSuccessData(String accessToken) {
		this.accessToken = accessToken;
	}
}
