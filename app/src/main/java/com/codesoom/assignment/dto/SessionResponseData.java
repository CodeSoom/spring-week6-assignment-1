package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionResponseData {
    private String accessToken;
}
