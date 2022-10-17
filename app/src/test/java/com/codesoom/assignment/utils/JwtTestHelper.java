package com.codesoom.assignment.utils;

import java.util.regex.Pattern;

/**
 * JWT 테스트를 위한 헬퍼
 */
public class JwtTestHelper {
    private static final String SECRET = "12345678901234567890123456789010";
    private static final String TOKEN_PATTERN = "^[\\w-]+\\.[\\w-]+\\.[\\w-]+$";

    public static String getSecret() {
        return SECRET;
    }

    public static boolean hasPattern(String token) {
        return Pattern.matches(TOKEN_PATTERN, token);
    }
}
