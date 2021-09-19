package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoData {
    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b")
    @Mapping("email")
    private String email;

    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;
}
