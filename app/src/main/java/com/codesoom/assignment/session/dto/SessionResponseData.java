package com.codesoom.assignment.session.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class SessionResponseData {
    private String accessToken;

    public SessionResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
