package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 유저 등록 정보를 가지고 있습니다.
 */
@Getter
public class UserRegistrationData {
    @NotBlank
    @Size(min = 3)
    private final String email;

    @NotBlank
    private final String name;

    @NotBlank
    @Size(min = 4, max = 1024)
    private final String password;

    @Builder
    public UserRegistrationData(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * 유저 정보를 유저로 바꿔 리턴한다.
     *
     * @return 유저
     */
    public User toUser() {
        return User.builder()
                .email(this.email)
                .name(this.name)
                .password(this.password)
                .build();
    }
}
