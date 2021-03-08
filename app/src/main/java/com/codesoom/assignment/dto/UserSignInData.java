package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Accessors(fluent = true)
@FieldDefaults(makeFinal = true)
@Getter
public class UserSignInData {
    @NotBlank
    @Email
    @Mapping("email")
    @JsonProperty("email")
    String email;

    @NotBlank
    @Mapping("password")
    @JsonProperty("password")
    String password;

    @JsonCreator
    public UserSignInData(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password
    ) {
        this.email = email;
        this.password = password;
    }
}
