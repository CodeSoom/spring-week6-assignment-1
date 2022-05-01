package com.codesoom.assignment.token;

import lombok.Builder;

import java.util.Date;

/**
 * JWT 생성시 필요한 속성 정의
 */
public class JsonWebTokenAttribute {

    private final String jwtId;

    private final JsonWebTokenExpireDate jwtExpireDate;

    @Builder
    public JsonWebTokenAttribute(String jwtId, Integer expireHour, Integer expireMinute, Integer expireSecound) {
        this.jwtId = jwtId;

        this.jwtExpireDate = new JsonWebTokenExpireDate(expireHour, expireMinute, expireSecound);
    }

    public String getJwtId() {
        return jwtId;
    }

    public Date getExpireDate() {
        return jwtExpireDate.getJwtExpireDate();
    }
}
