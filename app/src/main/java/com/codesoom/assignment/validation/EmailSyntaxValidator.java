package com.codesoom.assignment.validation;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailSyntaxValidator implements ConstraintValidator<Email, String> {

    private static final String DEFAULT_MESSAGE = "올바른 이메일 주소를 입력하세요.";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Strings.isBlank(value)) {
            addConstraintViolation(context, "이메일을 입력하세요.");
            return false;
        }

        addConstraintViolation(context, DEFAULT_MESSAGE);
        if (!value.contains("@")) {
            return false;
        }

        String[] split = value.split("@");
        if (split.length != 2) {
            return false;
        }

        return validateLocalPart(split[0]) && validateDomainPart(split[1]);
    }

    private boolean validateLocalPart(String localPart) {
        if (localPart.length() > 64) {
            return false;
        }
        final String regex = "(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(localPart);

        return matcher.matches();
    }

    private boolean validateDomainPart(String domainPart) {
        final String regex
                = "((\\[\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}])|(([a-zA-Z\\-\\d]+\\.)+[a-zA-Z]{2,}))";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(domainPart);

        return matcher.matches();
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}
