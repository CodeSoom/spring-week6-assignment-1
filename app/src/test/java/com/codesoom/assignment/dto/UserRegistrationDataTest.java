package com.codesoom.assignment.dto;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class UserRegistrationDataTest {
    @ParameterizedTest
    @AutoSource
    void creationWithBuilder(
            String email,
            String name,
            String password) {
        var userRegistrationData = UserRegistrationData.builder()
                                                       .email(email)
                                                       .name(name)
                                                       .password(password)
                                                       .build();

        assertThat(userRegistrationData.getEmail()).isEqualTo(email);
        assertThat(userRegistrationData.getName()).isEqualTo(name);
        assertThat(userRegistrationData.getPassword()).isEqualTo(password);
    }
}
