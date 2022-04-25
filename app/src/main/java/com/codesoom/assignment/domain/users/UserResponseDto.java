package com.codesoom.assignment.domain.users;

import lombok.Getter;

/**
 * 회원 정보 응답 시 사용할 DTO 클래스.
 */
@Getter
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String password;

    public UserResponseDto() {
    }

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

}
