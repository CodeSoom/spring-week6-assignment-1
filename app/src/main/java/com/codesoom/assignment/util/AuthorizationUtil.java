package com.codesoom.assignment.util;

import com.codesoom.assignment.errors.InvalidAccessTokenException;
import org.apache.commons.lang3.StringUtils;

public class AuthorizationUtil {

	private AuthorizationUtil() {
	}

	public static String extractTokenWithBearer(String authorization) {
		if (authorization == null || StringUtils.isBlank(authorization)) {
			throw new InvalidAccessTokenException(authorization);
		}

		String token = authorization.substring("Bearer ".length());
		if (StringUtils.isBlank(token)) {
			throw new InvalidAccessTokenException(authorization);
		}
		return token;
	}

}
