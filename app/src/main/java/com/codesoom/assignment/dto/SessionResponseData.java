package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 엑세스 토큰 응답객체.
 */
@Getter
@Builder
public class SessionResponseData {

    private String accessToken;
}
