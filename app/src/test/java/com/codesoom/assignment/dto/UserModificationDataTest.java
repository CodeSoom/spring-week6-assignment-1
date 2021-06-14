package com.codesoom.assignment.dto;

import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class UserModificationDataTest {
    @ParameterizedTest
    @AutoSource
    void creationWithBuilder(
            String name,
            String password) {
        var userModificationData = UserModificationData.builder()
                                                       .name(name)
                                                       .password(password)
                                                       .build();

        assertThat(userModificationData.getName()).isEqualTo(name);
        assertThat(userModificationData.getPassword()).isEqualTo(password);
    }
}
