package com.codesoom.assignment.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResultData {
    private Long id;

    private String email;

    private String name;
}
