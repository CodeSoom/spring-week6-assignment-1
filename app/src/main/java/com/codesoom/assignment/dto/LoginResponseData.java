package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginResponseData {
    private final String accessToken;

    @JsonCreator
    public LoginResponseData(@JsonProperty String accessToken) {
        this.accessToken = accessToken;
    }
}
