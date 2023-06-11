package com.codesoom.assignment.errors;

public class InvalidAccessTokenException extends RuntimeException {

	private final String INVALID_TOKEN_MSG = "Invalid Access Token : ";

	public InvalidAccessTokenException(String token) {
		this(token, null);
	}

	public InvalidAccessTokenException(String token, Throwable throwable) {
		super("Invalid Access Token : " + token, throwable);
	}
}
