package com.codesoom.assignment.exception;

public class DecodingInValidTokenException extends RuntimeException {
	public DecodingInValidTokenException(String token) {
		super(token + " is InValidToken");
	}
}
