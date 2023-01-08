package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * 유저로부터 이메일과 비밀번호를 받는 용도의 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginData {
    @Email
    private String email;

    @Size(min = 4, max = 1024)
    private String password;
}
