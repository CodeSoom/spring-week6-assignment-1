package com.codesoom.assignment.errors;

import java.util.Date;

public class AccessTokenExpiredException extends RuntimeException {
	public AccessTokenExpiredException(Date expiration) {
		super("Token Expired : " + expiration);
	}
}
