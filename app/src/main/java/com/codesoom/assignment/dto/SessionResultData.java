package com.codesoom.assignment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SessionResultData {
    private String accessToken;

    @Builder
    public SessionResultData(String accessToken) {
        this.accessToken = accessToken;
    }
}
