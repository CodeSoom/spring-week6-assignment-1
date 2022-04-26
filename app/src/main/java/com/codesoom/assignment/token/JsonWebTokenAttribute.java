package com.codesoom.assignment.token;

import lombok.Builder;
import lombok.Getter;

/**
 * JWT 생성시 필요 속성
 */
@Getter
public class JsonWebTokenAttribute {

    private final Long id;

    private final Integer expireMinute;

    @Builder
    public JsonWebTokenAttribute(Long id, Integer expireMinute) {
        this.id = id;
        this.expireMinute = expireMinute;
    }
}
