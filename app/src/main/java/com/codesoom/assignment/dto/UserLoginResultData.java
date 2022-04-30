package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResultData {
    private String accessToken;

    @Builder
    public UserLoginResultData(String accessToken) {
        this.accessToken = accessToken;
    }
}
