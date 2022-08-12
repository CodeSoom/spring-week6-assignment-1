package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResultData {
    private final String email;
    private final String name;

    @Builder
    public UserResultData(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserResultData from(User user) {
        return UserResultData.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
