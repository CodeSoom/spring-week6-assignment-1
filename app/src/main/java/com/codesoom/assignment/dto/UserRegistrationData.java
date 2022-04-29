package com.codesoom.assignment.dto;

import com.codesoom.assignment.errors.InvalidPasswordException;
import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;

@Getter
public class UserRegistrationData {
    @NotBlank
    @Size(min = 3)
    @Mapping("email")
    private final String email;

    @NotBlank
    @Mapping("name")
    private final String name;

    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private final String password;

    @Builder
    @ConstructorProperties({"email", "name", "password"})
    public UserRegistrationData(String email, String name, String password) {

        validatePassword(password);

        this.email = email;
        this.name = name;
        this.password = password;
    }

    // TODO - 리팩토링 예정 - PR 전이라 우선 커밋 합니다..
    private void validatePassword(final String password) {

        int consecutiveNumberCount = 0;

        char beforeCharacter = 0;

        for (char character : password.toCharArray()) {
            if(character < 48 || character > 57) {
                consecutiveNumberCount = 0;
                continue;
            }

            if(consecutiveNumberCount == 0) {
                consecutiveNumberCount = 1;
            } else if(character == (beforeCharacter + 1)) {
                consecutiveNumberCount = consecutiveNumberCount + 1;
            }

            if(consecutiveNumberCount == 3) {
                throw new InvalidPasswordException("비밀번호가 연속되는 3개의 숫자를 가지고 있습니다");
            }
            beforeCharacter = character;
        }
    }
}
