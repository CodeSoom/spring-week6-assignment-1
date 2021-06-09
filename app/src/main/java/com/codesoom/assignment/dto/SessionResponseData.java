package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionResponseData {
    private String accessToken;

    private SessionResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
