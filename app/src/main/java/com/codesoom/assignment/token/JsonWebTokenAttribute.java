package com.codesoom.assignment.token;

import lombok.Builder;
import lombok.Getter;

/**
 * JWT 생성시 필요한 속성 정의
 */
@Getter
public class JsonWebTokenAttribute {

    private final String jwtId;

    private final Integer expireMinute;

    @Builder
    public JsonWebTokenAttribute(String jwtId, Integer expireMinute) {
        this.jwtId = jwtId;
        this.expireMinute = expireMinute;
    }
}
