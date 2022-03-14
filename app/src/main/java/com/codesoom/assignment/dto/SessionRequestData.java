package com.codesoom.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SessionRequestData {

    @NotBlank
    @Size(min = 3)
    private String email;

    @NotBlank
    @Size(min = 4, max = 1024)
    private String password;

    @Builder
    public SessionRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
