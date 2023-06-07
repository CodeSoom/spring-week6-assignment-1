package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class LoginData {

	private String email;
	private String password;

	public LoginData(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
