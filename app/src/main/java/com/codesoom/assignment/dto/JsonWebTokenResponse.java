package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * JWT 전송에 필요한 데이터 정의
 */
@Getter
public class JsonWebTokenResponse {

    private final String accessToken;

    public JsonWebTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
