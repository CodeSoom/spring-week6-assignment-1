package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 정보 응답.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResultData {
    private Long id;

    private String email;

    private String name;
}
