package com.codesoom.assignment.token;

import lombok.Builder;

/**
 * JWT 생성시 필요한 속성 정의
 */
public class JsonWebTokenAttribute {

    private final String jwtId;

    private final JsonWebTokenExpireMinute jwtExpireMinute;

    @Builder
    public JsonWebTokenAttribute(String jwtId, Integer expireMinute) {
        this.jwtId = jwtId;
        this.jwtExpireMinute = new JsonWebTokenExpireMinute(expireMinute);
    }

    public String getJwtId() {
        return jwtId;
    }

    public Integer getJwtExpireMinute() {
        return jwtExpireMinute.getExpireMinute();
    }
}
