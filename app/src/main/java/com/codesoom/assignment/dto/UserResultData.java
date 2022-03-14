package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResultData {
    private Long id;

    private String email;

    private String name;

    @Builder
    public UserResultData(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
