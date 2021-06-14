package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 사용자 로그인 정보.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginData {

    @NotBlank
    @Email
    @Mapping("email")
    private String email;

    @NotBlank
    @Size(min = 4, max = 20)
    @Mapping("password")
    private String password;
}
