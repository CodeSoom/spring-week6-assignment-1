package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserLoginData {
    @NotBlank
    @Size(min = 3)
    @Mapping("email")
    private final String email;

    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private final String password;

    @JsonCreator
    public UserLoginData(@JsonProperty("email") String email,
                         @JsonProperty("password") String password){
        this.email = email;
        this.password = password;
    }
}
