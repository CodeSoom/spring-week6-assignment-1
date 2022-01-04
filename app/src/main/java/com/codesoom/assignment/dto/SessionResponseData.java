package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SessionResponseData {

    private String accessToken;

    @Builder
    public SessionResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
