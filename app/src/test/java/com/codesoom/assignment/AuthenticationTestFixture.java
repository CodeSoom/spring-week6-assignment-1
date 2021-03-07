package com.codesoom.assignment;

import com.codesoom.assignment.domain.User;

public class AuthenticationTestFixture {
    public static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.5VoLz2Ug0E6jQK_anP6RND1YHpzlBdxsR2ORgYef_aQ";
    public static final String SECRET_KEY = "qwertyuiopqwertyuiopqwertyuiopqw";

    public static final User GENERAL_USER = User.builder()
            .email("rlawlstjd@gmail.com")
            .password("12345")
            .id(1L)
            .build();
    public static final User NON_EXISTENT_USER = User.builder()
            .email("test@gmail.com")
            .password("123456")
            .build();
}
