package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginResult {
    private final String accessToken;

    @JsonCreator
    public LoginResult(@JsonProperty String accessToken) {
        this.accessToken = accessToken;
    }
}
