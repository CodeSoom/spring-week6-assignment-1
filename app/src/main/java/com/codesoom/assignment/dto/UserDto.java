package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@lombok.Generated
public class UserDto {

    @lombok.Generated
    @Getter
    @Setter
    @ToString
    public static class RequestParam {

        @NotBlank(message = "이름은 필수항목 입니다.")
        private String name;

        @NotBlank(message = "패스워드는 필수항목 입니다.")
        private String password;

        @NotBlank(message = "이메일은 필수항목 입니다.")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        private String email;
    }

    @lombok.Generated
    @Getter
    @Setter
    @ToString
    public static class UpdateParam {

        @NotBlank(message = "이름은 필수항목 입니다.")
        private String name;

        @NotBlank(message = "패스워드는 필수항목 입니다.")
        private String password;

        private String email;

    }

    @lombok.Generated
    @Getter
    @ToString
    public static class UserInfo {
        private final Long id;

        private final String name;

        private final String password;

        private final String email;

        public UserInfo(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.password = user.getPassword();
            this.email = user.getEmail();
        }
    }
}
