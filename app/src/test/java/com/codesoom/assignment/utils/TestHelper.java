package com.codesoom.assignment.utils;

import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserLoginData;

public class TestHelper {

    public static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.neCsyNLzy3lQ4o2yliotWT06FwSGZagaHpKdAkjnGGw";
    public static final String AUTH_NAME = "AUTH_NAME";
    public static final String AUTH_EMAIL = "auth@foo.com";
    public static final String INVALID_EMAIL = AUTH_EMAIL + "INVALID";
    public static final String AUTH_PASSWORD = "12345678";
    public static final String INVALID_PASSWORD = AUTH_PASSWORD + "INVALID";

    public static UserLoginData AUTH_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(AUTH_PASSWORD)
            .build();

    public static User AUTH_USER = User.builder()
            .name(AUTH_NAME)
            .email(AUTH_EMAIL)
            .password(AUTH_PASSWORD)
            .build();


    public static UserLoginData IS_NOT_EXISTS_USER_DATA = UserLoginData.builder()
            .email(INVALID_EMAIL)
            .password(AUTH_PASSWORD).build();

    public static UserLoginData INVALID_PASSWORD_USER_DATA = UserLoginData.builder()
            .email(AUTH_EMAIL)
            .password(INVALID_PASSWORD)
            .build();
}
