package com.codesoom.assignment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SessionCreateData {
    @NotBlank(message = "email 값은 필수입니다")
    @Size(min=3)
    private String email;

    @NotBlank(message = "password 값은 필수입니다")
    @Size(min = 4, max = 1024)
    private String password;

    @Builder
    public SessionCreateData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
