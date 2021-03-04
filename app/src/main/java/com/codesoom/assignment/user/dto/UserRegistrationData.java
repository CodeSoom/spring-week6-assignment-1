package com.codesoom.assignment.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 사용자 생성 요청.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationData {

    /**
     * 사용자 이메일.
     */
    @NotBlank
    @Size(min = 3)
    @Mapping("email")
    private String email;

    /**
     * 사용자 이름.
     */
    @NotBlank
    @Mapping("name")
    private String name;

    /**
     * 사용자 비밀번호.
     */
    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;
}
