package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class UserDto{
    private Long id;

    @NotBlank(message = "이름을 입력해주세요")
    private String username;

    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    public static User from(UserDto userDto) {
        return User.of(
                userDto.getUsername(),
                userDto.email,
                userDto.getPassword()
        );
    }

}