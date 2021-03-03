package com.codesoom.assignment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class UserResultData {
    private Long id;

    private String name;

    private String email;

    private String password;

    private boolean deleted;

    @Builder
    public UserResultData(Long id, String name, String email, String password, boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deleted = deleted;
    }
}
