package com.codesoom.assignment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@lombok.Generated
public class AuthDto {

    @lombok.Generated
    @Getter
    @Setter
    @ToString
    public static class LoginParam {

        @NotBlank(message = "이메일은 필수항목 입니다.")
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        private String email;

        @NotBlank(message = "패스워드는 필수항목 입니다.")
        private String password;
    }


    @lombok.Generated
    @Getter
    @ToString
    public static class SessionInfo {
        private final String accessToken;

        public SessionInfo(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
