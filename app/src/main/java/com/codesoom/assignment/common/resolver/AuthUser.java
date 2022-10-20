package com.codesoom.assignment.common.resolver;

import lombok.Getter;
import lombok.ToString;

@lombok.Generated
@Getter
@ToString
public class AuthUser {
    private final Long userId;

    public AuthUser(Long userId) {
        this.userId = userId;
    }
}
