package com.codesoom.assignment.validator;

import com.codesoom.assignment.dto.UserLoginData;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginValidatorTest {

    @Test
    void beanValidation(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserLoginData>> violationSet = validator.validate(new UserLoginData("1" , "12"));
        violationSet.forEach(System.out::println);
    }
}
