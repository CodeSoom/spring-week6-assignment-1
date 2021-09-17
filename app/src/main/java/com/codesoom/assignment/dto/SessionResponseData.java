package com.codesoom.assignment.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class SessionResponseData {

    private String accessToken;

    public SessionResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
