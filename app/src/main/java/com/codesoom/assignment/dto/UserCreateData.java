package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
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
public class UserCreateData {
    @Mapping("name")
    @NotBlank(message = "name 값은 필수입니다")
    private String name;

    @Mapping("email")
    @NotBlank(message = "email 값은 필수입니다")
    @Size(min=3)
    private String email;

    @Mapping("password")
    @NotBlank(message = "password 값은 필수입니다")
    @Size(min = 4, max = 1024)
    private String password;

    @Builder
    public UserCreateData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
