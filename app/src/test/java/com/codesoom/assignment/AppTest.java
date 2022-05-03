package com.codesoom.assignment;

import com.codesoom.assignment.dto.UserLoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AppTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("ObjectMapper 의 readValue() 메서드에 Setter 가 필요 없다면, DTO 에도 기본으로는 필요 없다.")
    void jacksonTest() throws JsonProcessingException {
        UserLoginData loginData
                = objectMapper.readValue(
                    "{\"email\":\"validUser@google.com\",\"password\":\"12345678\"}",
                            UserLoginData.class
        );

        System.out.printf("%s, %s%n", loginData.getEmail(), loginData.getPassword());
    }
}
