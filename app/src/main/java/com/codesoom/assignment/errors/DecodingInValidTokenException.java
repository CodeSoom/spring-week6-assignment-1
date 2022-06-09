package com.codesoom.assignment.errors;

public class DecodingInValidTokenException extends RuntimeException {
	public DecodingInValidTokenException(String token) {
		super(token + " is InValidToken");
	}
}
