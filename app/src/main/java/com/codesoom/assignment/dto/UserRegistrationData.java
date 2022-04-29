package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;

@Getter
public class UserRegistrationData {
    @NotBlank
    @Size(min = 3)
    @Mapping("email")
    private final String email;

    @NotBlank
    @Mapping("name")
    private final String name;

    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private final String password;

    @Builder
    @ConstructorProperties({"email", "name", "password"})
    public UserRegistrationData(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
