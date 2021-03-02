package com.codesoom.assignment.auth.dto;

import lombok.Getter;

/**
 * 사용자인증 응답 객체.
 */
@Getter
public class SessionResponseData {
    /**
     * 사용자 인증 토큰.
     */
    private final String accessToken;

    public SessionResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
