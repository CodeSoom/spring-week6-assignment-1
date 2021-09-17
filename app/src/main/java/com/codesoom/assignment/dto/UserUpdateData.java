package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserUpdateData {

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @Builder
    public UserUpdateData(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
