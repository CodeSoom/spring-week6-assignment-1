package com.codesoom.assignment.utils;

import com.codesoom.assignment.dto.AuthDto;

import java.util.Random;
import java.util.UUID;

import static com.codesoom.assignment.utils.LoginSampleFactory.FieldName.EMAIL;
import static com.codesoom.assignment.utils.LoginSampleFactory.FieldName.PASSWORD;


public class LoginSampleFactory {

    private static final Random random = new Random();

    public static AuthDto.LoginParam createLoginParam() {
        final AuthDto.LoginParam loginParam = new AuthDto.LoginParam();
        loginParam.setEmail("test" + random.nextInt(100) + "@gmail.com");
        loginParam.setPassword(UUID.randomUUID().toString());

        return loginParam;
    }

    public static AuthDto.LoginParam createRequestParamWith(FieldName fieldName, ValueType valueType) {
        final AuthDto.LoginParam loginParam = new AuthDto.LoginParam();
        String testValue;

        if (valueType == ValueType.NULL) {
            testValue = null;
        } else if (valueType == ValueType.EMPTY) {
            testValue = "";
        } else {
            testValue = "  ";
        }

        loginParam.setPassword(fieldName == PASSWORD ? testValue : UUID.randomUUID().toString());
        loginParam.setEmail(fieldName == EMAIL ? testValue : "test" + random.nextInt(100) + "@gmail.com");

        return loginParam;
    }
    public enum FieldName {
        EMAIL("email", "이메일"),

        PASSWORD("password", "패스워드");

        private String name;
        private String description;

        FieldName(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum ValueType {
        NULL, EMPTY, BLANK
    }
}
