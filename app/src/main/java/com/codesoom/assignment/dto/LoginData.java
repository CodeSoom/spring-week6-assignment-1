package com.codesoom.assignment.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode
@Getter
public class LoginData {

	@NotBlank
	private final String email;
	@NotBlank
	private final String password;

	public LoginData(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
