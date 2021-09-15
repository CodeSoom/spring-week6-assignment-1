package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestData {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
