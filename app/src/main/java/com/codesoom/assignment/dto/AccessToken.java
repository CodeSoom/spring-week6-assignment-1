package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * 액세스 토큰.
 */
@Getter
public class AccessToken {

    public final String accessToken;

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
