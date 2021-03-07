package com.codesoom.assignment.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 인증 응답.
 */
@Getter
@NoArgsConstructor
public class SessionResponseData {
    /**
     * 사용자 인증 토큰.
     */
    private String accessToken;

    public SessionResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
