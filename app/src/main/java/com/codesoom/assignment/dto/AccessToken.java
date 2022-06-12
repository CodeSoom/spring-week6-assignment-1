package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class AccessToken {
    public final String accessToken;

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
