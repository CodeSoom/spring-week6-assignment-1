package com.codesoom.assignment.session.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionResponseDto {
    private final String accessToken;

    @Builder
    public SessionResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
