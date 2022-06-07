package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class LoginData {
    private final String email;

    private final String password;

    @Builder
    @JsonCreator
    public LoginData(@JsonProperty("email") String email,
                     @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }
}
