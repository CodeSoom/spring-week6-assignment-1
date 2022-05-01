package com.codesoom.assignment.token;

/**
 * JWT 만료 분
 */
public class JsonWebTokenExpireMinute {

    private final Integer expireMinute;

    public JsonWebTokenExpireMinute(Integer expireMinute) {
        this.expireMinute = expireMinute;
    }

    public Integer getExpireMinute() {
        return expireMinute;
    }
}
