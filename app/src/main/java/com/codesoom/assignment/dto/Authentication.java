package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Authentication {
    private Long id;
    private String email;

    @Builder
    public Authentication(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
