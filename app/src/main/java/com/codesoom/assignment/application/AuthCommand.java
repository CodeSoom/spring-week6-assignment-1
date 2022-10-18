package com.codesoom.assignment.application;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@lombok.Generated
public class AuthCommand {

    @lombok.Generated
    @Getter
    @Builder
    @ToString
    public static class Login {

        private final String email;

        private final String password;

    }

}
